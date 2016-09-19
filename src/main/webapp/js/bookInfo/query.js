/**
 * 
 */
$(document).ready(function(){
	loadScannerQR();
	document.getElementById('up').addEventListener('change', handleFileSelect,false);
	//查询
	$("#submit_book_id").click(function (){
		uploadPicResult=false;
		isCreater=false;
		showBookInfo();
	});
	
	//修改
	$("#modify_bookInfo").click(function (){
		modifyBookInfo();
	});
	//扫描code
	$("#scanner_book_id").click(function (){
		openScanner();
	});
	
	//保存
	$("#save_bookInfo").click(function (){
		saveBookInfo();
	});
	
	//手工录入
	$("#add_book_info").click(function (){
		isCreater=true;
		showInfoToSave();
		$("#book_id_info_input").val(bookIdAllContent);
	});
	
	init();
	
	$("#book_id_info_input").blur(function (){
		if(isCreater){
			bookIdAllContent=$("#book_id_info_input").val();
			if(!uploadPicResult){
				uploadCompressImg();
			}
		}
	});
});

var bookIdAllContent;
var isCreater=false;
var uploadPicResult=false;
function init(){
	$(".show_book_info").hide();
	$(".loading").hide();
	$("#up").hide();
	$("#add_book_info").hide();
}

function loadScannerQR(){
	$.ajax({
		url:"wechatVerifyCode.action",
		data:{
			wechatQRcode:'scannerBookInfo',
		},
		type:'get',
		dataType: 'json',
		success:function(res){
			if(res.success){
				var result=res.result;
				wx.config({
				    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
				    appId: 'wx56247977be201113', // 必填，公众号的唯一标识
				    timestamp: result.timestamp, // 必填，生成签名的时间戳
				    nonceStr: result.nonceStr, // 必填，生成签名的随机串
				    signature: result.signature,// 必填，签名，见附录1
				    jsApiList: ['scanQRCode'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
				});
	        }else{
	        	alert("获取密钥失败");
	        }
		},
		error:function(data){
			alert("获取密钥失败");
		}
	});
}

function openScanner(){
	wx.scanQRCode({
	    needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
	    scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
	    success: function (res) {
	    	var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
	    	var num=result.split(",")[1];
	    	if(typeof(num)=="string"){
	    		uploadPicResult=false;
	    		isCreater=false;
	    		$("#book_id").val(num);
	    		showBookInfo();
	    	}else{
	    		alert("扫描失败，请手动输入");
	    	}
		}
	});
}

function uploadCompressImg(){
	if(invalid(bookIdAllContent)){
		alert("请先填写图书编号");
		return ;
	}
	var base64=$("#book_photo_path").attr("src");
	$.ajax({
		url:"fileUploadBase64.action",
		data:{
			base64:base64,
			bookId:bookIdAllContent
		},
		type:'post',
		dataType: 'json',
		success:function(res){
			if(res.success){
	        	uploadPicResult=true;
	        }else{
	        	uploadPicResult=false;
	        }
		},
		error:function(data){
			uploadPicResult=false;
		}
	});
}


function handleFileSelect(evt) {
	var files = evt.target.files;
	for (var i = 0, f; f = files[i]; i++) {
		if (!f.type.match('image.*')) {
			continue;
		}
		var reader = new FileReader();
		reader.onload = (function(theFile) {
			return function(e) {
				var i = document.getElementById("book_photo_path");
				i.src = event.target.result;
				$(i).css('width', $(i).width()+ 'px');
				var quality =80;
				i.src = $.compress(i, quality).src;
				i.style.display = "block";
			};
		})(f);
		reader.readAsDataURL(f);
		uploadCompressImg();
	}
}
/**
 * 手工录入信息
 * 
 */
function showInfoToSave(){
	initCleanInfo();
	modifyBookInfo();
	$("#up").val("");
	$("#up").show();
	$("#modify_bookInfo").hide();
	$("#save_bookInfo").show();
}


/**
 * 图片上传
 * @returns {Boolean}
 */
function upload(){
    $.ajaxFileUpload({
        url:'fileUpload.action',
        secureuri:false,
        fileElementId:'up',
        dataType: 'json',
        success: function (data, status){
           if(data.status==1){
               alert(data.result);
           }else{
              alert("【提交失败！】");
           }
        },
        error: function (data, status, e){
                alert("【服务器异常，请连续管理员！】"+e);
        }
   });
    return false;
}

/**
 * 显示图书详情
 */
function showBookInfo(){
	bookIdAllContent="";
	$(".show_book_info").hide();
	
	if(invalid($("#book_id").val())){
		alert("请填入书本的条形码");
		return ;
	}
	$(".loading").show();
	$.ajax({
		url:"bookInfo.action",
		dataType: 'json',
		data:{
			bookId:$("#book_id").val()
		},
		type:'post',
		success:function(data){
			initCleanInfo();
			$("#add_book_info").hide();
			$(".loading").hide();
			if(data.success){
				var showInfoText=false;
				var bookInfo=data.result;
				if(bookInfo.bookFrom==1){//互联网渠道
					if(bookInfo.successByInternet){//查询成功
						showInfoText=true;
					}else{
						alert("没有查到该书本信息,请认真核对条形码是否有误,若确认条形码正确,请选择手工录入");
						bookIdAllContent=$("#book_id").val();
						$("#book_id_info_input").val(bookIdAllContent);
						$("#add_book_info").show();
					}
				}else if(bookInfo.bookFrom==0){
					showInfoText=true;
				}
				
				if(showInfoText){
					$("#book_id_info").html(bookInfo.bookId);
					$("#book_protocl").html(bookInfo.bookProtocl);
					$("#book_name").html(bookInfo.bookName);
					$("#book_concern").html(bookInfo.bookConcerm);
					$("#book_author").html(bookInfo.author);
					$("#order_prices").html(bookInfo.orderPrices/100+"元");
					$("#book_page_num").html(bookInfo.pageNum);
					if(bookInfo.bookCreateWay==2){
						$("#book_photo_path").attr('src',bookInfo.localPhotoPath);
					}else{
						$("#book_photo_path").attr('src',bookInfo.photoPath);
					}
					
					$("#book_id_info_input").val(bookInfo.bookId);
					$("#book_protocl_input").val(bookInfo.bookProtocl);
					$("#book_name_input").val(bookInfo.bookName);
					$("#book_concern_input").val(bookInfo.bookConcerm);
					$("#book_author_input").val(bookInfo.author);
					$("#order_prices_input").val(bookInfo.orderPrices);
					$("#book_page_num_input").val(bookInfo.pageNum);
					
					$(".show_book_info").show();
					$(".show_info").show();
					$(".modify_info").hide();
				}
				
				if(bookInfo.bookFrom==0){
					$("#modify_bookInfo").hide();
					$("#save_bookInfo").hide();
				}
			}else{
				$("#add_book_info").show();
				$(".loading").hide();
				alert(data.errorDesc);
			}
		},
		error:function(data){
			$("#add_book_info").show();
			alert("系统已累瘫,请稍后再试");
			initCleanInfo();
			$("#add_book_info").hide();
			$(".loading").hide();
		}
	});
}

/**
 * 清除
 */
function initCleanInfo(){
	$("#book_id_info_input").val("");
	$("#book_protocl_input").val("");
	$("#book_name_input").val("");
	$("#book_concern_input").val("");
	$("#book_author_input").val("");
	$("#order_prices_input").val("");
	$("#book_page_num_input").val("");
	
	$("#book_id_info").html("");
	$("#book_protocl").html("");
	$("#book_name").html("");
	$("#book_concern").html("");
	$("#book_author").html("");
	$("#order_prices").html("");
	$("#book_page_num").html("");
	$("#book_photo_path").attr('src',"");
}

/**
 * 储存书本详情
 */
function saveBookInfo(){
	
	if($("#book_id_info_input").val().trim()==""||$("#book_protocl_input").val().trim()==""||$("#book_name_input").val().trim()==""||$("#book_concern_input").val().trim()==""||$("#book_author_input").val().trim()==""||$("#order_prices_input").val().trim()==""||$("#book_page_num_input").val().trim()==""){
		alert("请将信息填写完整");
		return;
	}
	$(".loading").show();
	var data={
			bookId:$("#book_id_info_input").val(),
			bookProtocl:$("#book_protocl_input").val(),
			bookName:$("#book_name_input").val(),
			bookConcern:$("#book_concern_input").val(),
			bookAuthor:$("#book_author_input").val(),
			orderPrices:$("#order_prices_input").val(),
			bookPageNum:$("#book_page_num_input").val(),
		}
	if(!isCreater){
		data["bookPhotoPath"]=$("#book_photo_path").attr('src');
	}
	if(!uploadPicResult){
		uploadCompressImg();
	}
	$.ajax({
		url:'saveInfo.action',
		dataType:'json',
		type:'post',
		data:data,
		success:function(data){
			alert(data.errorDesc);
			if(data.success){
				$(".show_book_info").hide();
			}else{
				alert(data.errorDesc);
			}
			$(".loading").hide();
		},
		error:function(data){
			alert(data.errorDesc);
			$(".loading").hide();
		}
	});
}

/**
 * 修改
 */
function modifyBookInfo(){
	$(".show_book_info").show();
	$(".show_info").hide();
	$(".modify_info").show();
}