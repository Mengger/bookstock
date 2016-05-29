KFZ.util.charToUnicode=function(str){
	if(!str)return;
	
	var unicode='',i=0,len=(str=''+str).length;
	for(;i<len;i++){
		unicode+='k'+str.charCodeAt(i).toString(16).toLowerCase();
	}
}