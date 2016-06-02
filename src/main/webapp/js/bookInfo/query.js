
/**
 * 
 */
$(document).ready(function(){
	//查询
	$("#submit_book_id").click(function (){
		showBookInfo();
	});
	//修改
	$("#modify_bookInfo").click(function (){
		modifyBookInfo();
	});
	
	//保存
	$("#save_bookInfo").click(function (){
		saveBookInfo();
	});
	init();
	
});

function init(){
	$(".show_book_info").hide();
	$(".loading").hide();
	$("#add_book_info").hide();
}

/**
 * 显示图书详情
 */
function showBookInfo(){
	$(".show_book_info").hide();
	
	if(invalid($("#book_id").val())){
		alert("请填入书本的条形码");
		return ;
	}
	$(".loading").show();
	$.ajax({
		url:"../bookInfo.json",
		dataType: 'json',
		data:{
			bookId:$("#book_id").val()
		},
		success:function(data){
			$(".loading").hide();
			if(data.success){
				var bookInfo=data.result;
				$("#book_id_info").html(bookInfo.bookId);
				$("#book_protocl").html(bookInfo.bookProtocl);
				$("#book_name").html(bookInfo.bookName);
				$("#book_concern").html(bookInfo.bookConcerm);
				$("#book_author").html(bookInfo.author);
				$("#order_prices").html(bookInfo.orderPrices/100+"元");
				$("#book_page_num").html(bookInfo.pageNum);
				$("#book_photo_path").attr('src',bookInfo.photoPath);
				
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
			}else{
				$("#add_book_info").show();
				alert(data.errorDesc);
			}
		},
		error:function(data){
			$("#add_book_info").show();
			alert("系统已累瘫,请稍后再试");
		}
	});
}

/**
 * 储存书本详情
 */
function saveBookInfo(){
	$(".loading").show();
	$.ajax({
		url:'../saveInfo.json',
		dataType:'json',
		data:{
			bookId:$("#book_id_info_input").val(),
			bookProtocl:$("#book_protocl_input").val(),
			bookName:$("#book_name_input").val(),
			bookConcern:$("#book_concern_input").val(),
			bookAuthor:$("#book_author_input").val(),
			orderPrices:$("#order_prices_input").val(),
			bookPageNum:$("#book_page_num_input").val(),
			bookPhotoPath:$("#book_photo_path").attr('src')
		},
		success:function(data){
			alert(data.errorDesc);
			if(data.success){
				$(".show_book_info").hide();
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