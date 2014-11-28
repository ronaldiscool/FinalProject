CREATE SCHEMA `mafia`;
USE `mafia`;
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(100) DEFAULT NULL,
  `times_played` int(7) DEFAULT NULL,
  `overall_wins` int(7) DEFAULT NULL,
  `overall_losses` int(7) DEFAULT NULL,
  `times_as_mafia` int(7) DEFAULT NULL,
  `wins_as_mafia` int(7) DEFAULT NULL,
  `losses_as_mafia` int(7) DEFAULT NULL,
  `times_as_villager` int(7) DEFAULT NULL,
  `wins_as_villager` int(7) DEFAULT NULL,
  `losses_as_villager` int(7) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=198 DEFAULT CHARSET=latin1;