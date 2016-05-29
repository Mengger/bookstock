create table get_order(  
	create_time  date not null comment  '创建时间',  
	modify_time date not null  comment  '修改时间',  
	parent_id varchar(14) not null comment '父订单id',  
	order_id varchar(14) not null PRIMARY KEY comment '子订单id',  
	book_id varchar(15) not null comment '图书编号',  
	book_prices Integer comment '回收价格',  
	tip  Integer comment '回收小费',  
	employee_id  Integer comment '雇员id',  
	book_count Integer comment '图书数量',  
	status Integer comment '订单状态(-1.订单作废 0.读取完毕 1.雇员回收完成)'
) comment '订单详情表';

create table book_info( 
	info_create_time  date not null comment  '创建时间', 
	info_modify_time date not null  comment  '修改时间', 
	book_id varchar(15) not null PRIMARY KEY comment '图书编号', 
	book_protocl varchar(5) comment '编号协议(ISBN、ISSN、ISRC)', 
	book_name varchar(50) not null comment '图书名称', 
	author varchar(20) comment '作者', 
	book_concerm  varchar(50) comment '出版社名称', 
	status  Integer comment '图书状态(1.正常  0.停收)', 
	book_type_id varchar(10) comment '图书类型id', 
	order_prices Integer comment '定价(单位:分)', 
	used_prices  Integer comment '二手书价(单位:分)', 
	photo_path varchar(50) comment '照片地址', 
	page_num varchar(5) comment '页数' 
) comment '图书属性表';


create table employee_info( 
	create_time  date not null comment  '创建时间',  
	modify_time date not null  comment  '修改时间',  
	id varchar(5) not null PRIMARY KEY comment '雇员id',
	name varchar(16) comment '雇员名字',
	id_card varchar(20) comment '身份证号码',
	birth_place varchar(50) comment '出生地',
	pwd varchar(20) comment '密码',
	manager_id varchar(5) comment '经理id',
	area_id varchar(8) comment '区域id',
	school_id varchar(8) comment '学校id',
	photo_path varchar(50) comment '图片',
	id_card_path varchar(50) comment '身份证图片',
	info varchar(50) comment '备注信息(地址)'
) comment '雇员信息表';

create table school_info( 
	create_time  date not null comment  '创建时间',  
	modify_time date not null  comment  '修改时间',  
	school_id varchar(7) comment '学校id',
	school_name varchar(50) comment '学校名称',
	school_address varchar(50) comment '学校地址(主要地址)',
	area_id varchar(3) comment '学校区块id',
	area_name varchar(50) comment '区块名称',
	area_address varchar(50) comment '区块地址',
	pox varchar(20) comment '区块经纬度(经度｜纬度)',
	head_id varchar(5) comment '区块负责任id'
) comment '学校信息表';

create table sale_order( 
	create_time  date not null comment  '创建时间',  
	modify_time date not null  comment  '修改时间',  
	order_id varchar(14) comment '订单号',
	book_id varchar(15) comment '书编号',
	prices Integer comment '价格（分)',
	order_num varchar(6) comment '数量',
	buyer_id varchar(10) comment '买家id',
	status Integer comment '订单状态(0.待交货，-1.点单作废 1.订单完成)'
) comment '买家订单表';

create table buyer_info( 
	create_time  date not null comment  '创建时间',  
	modify_time date not null  comment  '修改时间', 
	buyer_id varchar(10) not null  comment '买家id',
	buyer_name varchar(20) comment '买家姓名',
	buyer_id_card varchar(20) comment '买家身份证',
	buyer_address varchar(50) comment '买家地址',
	buyer_phone varchar(11) comment '买家电话',
	buyer_qq varchar(12) comment 'qq',
	buyer_e_mail varchar(20) comment 'e_mail'
) comment '买家信息表';