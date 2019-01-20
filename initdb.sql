

CREATE TABLE `sec_user` (
	`id` bigint(20) NOT NULL COMMENT '用户id,手机号码',
	`nickname` VARCHAR(255) NOT NULL ,
	`password` VARCHAR(32) DEFAULT NULL COMMENT 'MD5(MD5(pass明文 + 固定salt)+salt)',
	`salt` VARCHAR(10) DEFAULT 10,
	`head` VARCHAR(128) DEFAULT NULL COMMENT '头像，云存储id',
	`register_date` datetime DEFAULT NULL COMMENT '注册时间',
	`last_login_date` datetime DEFAULT NULL COMMENT '上次登录时间',
	`login_count` int(11) DEFAULT '0' COMMENT '登录次数',
	PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `goods` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
	`goods_name` VARCHAR(16) DEFAULT NULL COMMENT '商品名称',
	`goods_title` VARCHAR(64) DEFAULT NULL COMMENT '商品标题',
	`goods_img` VARCHAR(64) DEFAULT NULL COMMENT '商品的图片',
	`goods_detail` LONGTEXT COMMENT '商品详情介绍',
	`goods_price` DECIMAL(10,2) DEFAULT '0.00' COMMENT '商品单价',
	`goods_stock` int(11) DEFAULT '0' COMMENT '商品库存，-1表示没有限制',
	PRIMARY KEY(`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sec_goods` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀的商品id',
	`goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
	`sec_price` DECIMAL(10,2) DEFAULT '0.00' COMMENT '秒杀价',
	`stock_count` int(11) DEFAULT NULL COMMENT '库存数量',
	`start_date` datetime DEFAULT NULL COMMENT '秒杀开始时间',
	`end_date` datetime DEFAULT NULL COMMENT '秒杀结束时间',
	PRIMARY KEY(`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `order_info` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
	`user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
	`goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
	`delivery_addr_id` bigint(20) DEFAULT NULL COMMENT '收货地址id',
	`goods_name` VARCHAR(16) DEFAULT NULL COMMENT '商品名称',
	`goods_count` int(11) DEFAULT NULL COMMENT '商品数量',
	`goods_price` DECIMAL(10,2) DEFAULT '0.00' COMMENT '商品单价',
	`order_channel` tinyint(4) DEFAULT '0' COMMENT '1pc 2android 3iOS',
	`status` tinyint(4) DEFAULT '0' COMMENT '订单状态 0新建未支付 1已支付 2已发货 3已收货 4已退款 5已完成',
	`create_date` datetime DEFAULT NULL COMMENT '订单创建时间',
	`pay_date` datetime DEFAULT NULL COMMENT '支付时间',
	PRIMARY KEY(`id`)
) ENGINE=INNODB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sec_order` (
	`id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀的商品id',
	`user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
	`order_id` bigint(20) DEFAULT NULL COMMENT '订单id',
	`goods_id` bigint(20) DEFAULT NULL COMMENT '商品id',
	PRIMARY KEY(`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

ALTER TABLE `seckill`.`sec_order`
	ADD UNIQUE INDEX `u_uid_gid`(`user_id`, `goods_id`) USING BTREE;