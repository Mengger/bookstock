/**
 * 判断数据类型
 * 若是defund  ''  null返回true
 */
function invalid(data){
	if(typeof(data)=="undefined"||data==null||data==""||data=="undefined")
		return true;
	return false;
}

function replaceInvalid(data,rtnData){
	if(invalid(data))
		return rtnData;
	return data;
}