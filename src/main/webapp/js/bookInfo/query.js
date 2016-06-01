
/**
 * 
 */
$(document).ready(function(){
	$("#submit_book_id").click(function (){
		showBookInfo();
	});
	$(".show_book_info").hide();
	$(".loading").hide();
});

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
				$(".loading").hide();
				$(".show_book_info").show();
			}else{
				alert(data.errorDesc);
			}
		}
	});
}