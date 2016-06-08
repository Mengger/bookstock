/**
 * 
 */

function handleFileSelect(evt) {
	var files = evt.target.files;
	for (var i = 0, f; f = files[i]; i++) {
		// Only process image files.
		if (!f.type.match('image.*')) {
			continue;
		}
		var reader = new FileReader();
		// Closure to capture the file information.
		reader.onload = (function(theFile) {
			return function(e) {
				//console.log(e.target.result);
				var i = document.getElementById("test");
				i.src = event.target.result;
				console.log($(i).width());
				console.log($(i).height());
				$(i).css('width', $(i).width()/2+ 'px');
				// $(i).css('height',$(i).height()/10+'px');
				console.log($(i).width());
				console.log($(i).height());
				var quality = 10;
				i.src = $.compress(i, quality).src;
				i.style.display = "block";
			};
		})(f);

		// Read in the image file as a data URL.
		reader.readAsDataURL(f);
	}
}

jQuery.extend( {
	compress : function(source_img_obj, quality, output_format) {
		var mime_type = "image/jpeg";
		if (output_format != undefined && output_format == "png") {
			mime_type = "image/png";
		}
		var cvs = document.createElement('canvas');
		// naturalWidth真实图片的宽度
		cvs.width = source_img_obj.naturalWidth;
		cvs.height = source_img_obj.naturalHeight;
		var ctx = cvs.getContext("2d").drawImage(source_img_obj, 0, 0);
		var newImageData = cvs.toDataURL(mime_type, quality / 100);
		var result_image_obj = new Image();
		result_image_obj.src = newImageData;
		return result_image_obj;
	}
})