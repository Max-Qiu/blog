
CREATE DATABASE /*!32312 IF NOT EXISTS*/ `blog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `blog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `article` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '文章主键ID',
  `label_ids` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签ID',
  `label_names` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章标题',
  `md` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'md源码',
  `text` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '纯文字',
  `view` int NOT NULL COMMENT '浏览',
  `top` tinyint(1) NOT NULL COMMENT '置顶 0否 1是',
  `show` tinyint(1) NOT NULL COMMENT '展示 0否 1是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_update_time` (`update_time`) USING BTREE,
  KEY `index_top` (`top`,`view`,`create_time`) USING BTREE,
  KEY `index_page` (`top`,`create_time`) USING BTREE,
  KEY `idx_show` (`show`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文章';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `block_view` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` tinyint unsigned NOT NULL COMMENT '类型 1浏览器标识 2IP运营商',
  `condition` tinyint unsigned NOT NULL COMMENT '条件 1= 2like',
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '值',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='屏蔽访问';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discuss` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `article_id` int unsigned NOT NULL COMMENT '文章ID',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `tier` tinyint unsigned NOT NULL COMMENT '层级',
  `revert_id` int unsigned NOT NULL COMMENT '回复的用户ID',
  `check` tinyint(1) NOT NULL COMMENT '是否审核过的 0否 1是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_article_id_check` (`article_id`,`check`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='评论';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ip_info` (
  `id` bigint unsigned NOT NULL COMMENT '主键ID（即IPV4转long）',
  `str` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字符串（即IPV4字符串）',
  `country` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '国家',
  `province` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '省份/自治区/直辖市',
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '地级市',
  `county` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '区/县',
  `operator` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '运营商',
  `postal_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '邮政编码',
  `area_code` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '地区区号',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='IP地址信息缓存';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `label` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '标签主键',
  `name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名称',
  `count` int unsigned NOT NULL COMMENT '文章数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='标签';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_article` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `article_id` int unsigned NOT NULL COMMENT '文章ID',
  `mark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户标记',
  `user_agent` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '浏览器标识',
  `referer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '来源',
  `ip` bigint unsigned NOT NULL COMMENT '访问者IP（long）',
  `blocked` tinyint unsigned NOT NULL DEFAULT '0' COMMENT '屏蔽 0否 1是',
  `block_id` int DEFAULT NULL COMMENT '屏蔽原因ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_aid_mark` (`article_id`,`mark`) USING BTREE,
  KEY `idx_blocked` (`blocked`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='文章访问日志';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_login` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` int unsigned NOT NULL COMMENT '用户ID',
  `ip` bigint unsigned NOT NULL COMMENT '登录IP（long）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间（登录时间）',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `accountId_index` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='登录日志';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `nickname` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='随机昵称';
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `nickname` VALUES (1,'及时雨-宋江');
INSERT INTO `nickname` VALUES (2,'玉麒麟-卢俊义');
INSERT INTO `nickname` VALUES (3,'智多星-吴用');
INSERT INTO `nickname` VALUES (4,'入云龙-公孙胜');
INSERT INTO `nickname` VALUES (5,'大刀-关胜');
INSERT INTO `nickname` VALUES (6,'豹子头-林冲');
INSERT INTO `nickname` VALUES (7,'霹雳火-秦明');
INSERT INTO `nickname` VALUES (8,'双鞭-呼延灼');
INSERT INTO `nickname` VALUES (9,'小李广-花荣');
INSERT INTO `nickname` VALUES (10,'小旋风-柴进');
INSERT INTO `nickname` VALUES (11,'扑天雕-李应');
INSERT INTO `nickname` VALUES (12,'美髯公-朱仝');
INSERT INTO `nickname` VALUES (13,'花和尚-鲁智深');
INSERT INTO `nickname` VALUES (14,'行者-武松');
INSERT INTO `nickname` VALUES (15,'双枪将-董平');
INSERT INTO `nickname` VALUES (16,'没羽箭-张清');
INSERT INTO `nickname` VALUES (17,'青面兽-杨志');
INSERT INTO `nickname` VALUES (18,'金枪手-徐宁');
INSERT INTO `nickname` VALUES (19,'急先锋-索超');
INSERT INTO `nickname` VALUES (20,'神行太保-戴宗');
INSERT INTO `nickname` VALUES (21,'赤发鬼-刘唐');
INSERT INTO `nickname` VALUES (22,'黒旋风-李逵 ');
INSERT INTO `nickname` VALUES (23,'九纹龙-史进');
INSERT INTO `nickname` VALUES (24,'没遮拦-穆弘');
INSERT INTO `nickname` VALUES (25,'插翅虎-雷横');
INSERT INTO `nickname` VALUES (26,'混江龙-李俊');
INSERT INTO `nickname` VALUES (27,'立地太岁-阮小二');
INSERT INTO `nickname` VALUES (28,'船火儿-张横');
INSERT INTO `nickname` VALUES (29,'短命二郎-阮小五');
INSERT INTO `nickname` VALUES (30,'浪里白条-张顺');
INSERT INTO `nickname` VALUES (31,'活阎罗-阮小七');
INSERT INTO `nickname` VALUES (32,'病关索-杨雄');
INSERT INTO `nickname` VALUES (33,'拼命三郎-石秀');
INSERT INTO `nickname` VALUES (34,'两头蛇-解珍');
INSERT INTO `nickname` VALUES (35,'双尾蝎-解宝');
INSERT INTO `nickname` VALUES (36,'浪子-燕青');
INSERT INTO `nickname` VALUES (37,'神机军师-朱武');
INSERT INTO `nickname` VALUES (38,'镇三山-黄信');
INSERT INTO `nickname` VALUES (39,'病尉迟-孙立');
INSERT INTO `nickname` VALUES (40,'丑郡马-宣赞');
INSERT INTO `nickname` VALUES (41,'井木犴-郝思文');
INSERT INTO `nickname` VALUES (42,'百胜将-韩滔');
INSERT INTO `nickname` VALUES (43,'天目将-彭玘');
INSERT INTO `nickname` VALUES (44,'圣水将-单廷圭');
INSERT INTO `nickname` VALUES (45,'神火将-魏定国');
INSERT INTO `nickname` VALUES (46,'圣手书生-萧让');
INSERT INTO `nickname` VALUES (47,'铁面孔目-裴宣');
INSERT INTO `nickname` VALUES (48,'摩云金翅-欧鹏');
INSERT INTO `nickname` VALUES (49,'火眼狻猊-邓飞');
INSERT INTO `nickname` VALUES (50,'锦毛虎-燕顺');
INSERT INTO `nickname` VALUES (51,'锦豹子-杨林');
INSERT INTO `nickname` VALUES (52,'轰天雷-凌振');
INSERT INTO `nickname` VALUES (53,'神算子-蒋敬');
INSERT INTO `nickname` VALUES (54,'小温侯-吕方');
INSERT INTO `nickname` VALUES (55,'赛仁贵-郭盛');
INSERT INTO `nickname` VALUES (56,'神医-安道全');
INSERT INTO `nickname` VALUES (57,'紫髯伯-皇甫端');
INSERT INTO `nickname` VALUES (58,'矮脚虎-王英');
INSERT INTO `nickname` VALUES (59,'一丈青-扈三娘');
INSERT INTO `nickname` VALUES (60,'丧门神-鲍旭');
INSERT INTO `nickname` VALUES (61,'混世魔王-樊瑞');
INSERT INTO `nickname` VALUES (62,'毛头星-孔明');
INSERT INTO `nickname` VALUES (63,'独火星-孔亮');
INSERT INTO `nickname` VALUES (64,'八臂哪吒-项充');
INSERT INTO `nickname` VALUES (65,'飞天大圣-李衮');
INSERT INTO `nickname` VALUES (66,'玉臂匠-金大坚');
INSERT INTO `nickname` VALUES (67,'铁笛仙-马麟');
INSERT INTO `nickname` VALUES (68,'出洞蛟-童威');
INSERT INTO `nickname` VALUES (69,'翻江蜃-童猛');
INSERT INTO `nickname` VALUES (70,'玉幡竿-孟康');
INSERT INTO `nickname` VALUES (71,'通臂猿-侯健');
INSERT INTO `nickname` VALUES (72,'跳涧虎-陈达');
INSERT INTO `nickname` VALUES (73,'白花蛇-杨春');
INSERT INTO `nickname` VALUES (74,'白面郎君-郑天寿');
INSERT INTO `nickname` VALUES (75,'九尾龟-陶宗旺');
INSERT INTO `nickname` VALUES (76,'铁扇子-宋清');
INSERT INTO `nickname` VALUES (77,'铁叫子-乐和');
INSERT INTO `nickname` VALUES (78,'花项虎-龚旺');
INSERT INTO `nickname` VALUES (79,'中箭虎-丁得孙');
INSERT INTO `nickname` VALUES (80,'小遮拦-穆春');
INSERT INTO `nickname` VALUES (81,'操刀鬼-曹正');
INSERT INTO `nickname` VALUES (82,'云里金刚-宋万');
INSERT INTO `nickname` VALUES (83,'摸着天-杜迁');
INSERT INTO `nickname` VALUES (84,'病大虫-薛永');
INSERT INTO `nickname` VALUES (85,'打虎将-李忠');
INSERT INTO `nickname` VALUES (86,'小霸王-周通');
INSERT INTO `nickname` VALUES (87,'金钱豹子-汤隆');
INSERT INTO `nickname` VALUES (88,'鬼脸儿-杜兴');
INSERT INTO `nickname` VALUES (89,'出林龙-邹渊');
INSERT INTO `nickname` VALUES (90,'独角龙-邹润');
INSERT INTO `nickname` VALUES (91,'旱地忽律-朱贵');
INSERT INTO `nickname` VALUES (92,'笑面虎-朱富');
INSERT INTO `nickname` VALUES (93,'金眼彪-施恩');
INSERT INTO `nickname` VALUES (94,'铁臂膊-蔡福');
INSERT INTO `nickname` VALUES (95,'一枝花-蔡庆');
INSERT INTO `nickname` VALUES (96,'催命判官-李立');
INSERT INTO `nickname` VALUES (97,'青眼虎-李云');
INSERT INTO `nickname` VALUES (98,'没面目-焦挺');
INSERT INTO `nickname` VALUES (99,'石将军-石勇');
INSERT INTO `nickname` VALUES (100,'小尉迟-孙新');
INSERT INTO `nickname` VALUES (101,'母大虫-顾大嫂');
INSERT INTO `nickname` VALUES (102,'菜园子-张青');
INSERT INTO `nickname` VALUES (103,'母夜叉-孙二娘');
INSERT INTO `nickname` VALUES (104,'活闪婆-王定六');
INSERT INTO `nickname` VALUES (105,'险道神-郁保四');
INSERT INTO `nickname` VALUES (106,'白日鼠-白胜');
INSERT INTO `nickname` VALUES (107,'鼓上蚤-时迁');
INSERT INTO `nickname` VALUES (108,'金毛犬-段景住');
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `series` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `token` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC COMMENT='用户';
/*!40101 SET character_set_client = @saved_cs_client */;
INSERT INTO `user` VALUES (1,'admin','$2a$10$gO0FsHC/RI.CDD45JpqN1eiR0AHs0/hk1cHNSbcRy0GXKcSPUKNgm','2021-07-01 00:00:00','2022-10-28 22:00:00');
