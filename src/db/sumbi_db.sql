-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 25, 2025 at 06:59 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sumbi_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `logs_table`
--

CREATE TABLE `logs_table` (
  `logs_id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL,
  `logs_action` varchar(50) NOT NULL,
  `logs_stamp` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `logs_table`
--

INSERT INTO `logs_table` (`logs_id`, `user_id`, `logs_action`, `logs_stamp`) VALUES
(259, 1, 'Logged in', '2025-05-25 15:28:16'),
(260, 1, 'Added student: Bryll Josh Parba', '2025-05-25 15:28:37'),
(262, 1, 'Logged in', '2025-05-25 15:30:17'),
(263, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 15:31:11'),
(264, 1, 'Added Violation: Cheating', '2025-05-25 15:32:29'),
(265, 1, 'Updated Violation: Cheating', '2025-05-25 15:33:11'),
(267, 1, 'Logged in', '2025-05-25 15:34:11'),
(268, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 15:34:36'),
(269, 1, 'Updated Violation: Cheating', '2025-05-25 15:34:52'),
(271, 1, 'Logged in', '2025-05-25 15:40:06'),
(272, 1, 'Added New Record for Violation ID: 17', '2025-05-25 15:40:33'),
(274, 1, 'Logged in', '2025-05-25 15:41:56'),
(276, 1, 'Logged in', '2025-05-25 15:43:21'),
(279, 1, 'Logged in', '2025-05-25 15:48:49'),
(280, 1, 'Updated Record for Record ID: 17', '2025-05-25 15:49:13'),
(282, 1, 'Logged in', '2025-05-25 15:51:06'),
(283, 1, 'Updated Record for Record ID: 17', '2025-05-25 15:51:22'),
(284, 1, 'Updated user: patobaob@gmail.com', '2025-05-25 15:51:56'),
(286, 1, 'Logged in', '2025-05-25 16:00:36'),
(287, 1, 'Updated user: patobaob@gmail.com', '2025-05-25 16:01:38'),
(288, 1, 'Logged out', '2025-05-25 16:02:11'),
(289, 2, 'Logged in', '2025-05-25 16:04:03'),
(290, 1, 'Logged in', '2025-05-25 16:04:43'),
(291, 1, 'Updated user: patobaob@gmail.com', '2025-05-25 16:05:11'),
(292, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 16:12:44'),
(294, 1, 'Logged in', '2025-05-25 16:16:00'),
(295, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 16:19:29'),
(296, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 16:27:58'),
(297, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 16:32:48'),
(298, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 16:33:07'),
(299, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 16:33:23'),
(300, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 16:35:52'),
(302, 1, 'Logged in', '2025-05-25 16:45:05'),
(303, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 16:45:13'),
(304, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 16:46:17'),
(306, 1, 'Logged in', '2025-05-25 16:52:58'),
(307, 1, 'Updated Violation: Cheating', '2025-05-25 16:53:10'),
(309, 1, 'Logged in', '2025-05-25 16:57:09'),
(310, 1, 'Updated Violation: Cheating', '2025-05-25 16:57:22'),
(311, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 16:57:41'),
(312, 1, 'Updated Student: Bryll Josh Parba', '2025-05-25 16:58:16'),
(313, 1, 'Added New Record for Violation ID: 17', '2025-05-25 17:00:58'),
(315, 1, 'Logged in', '2025-05-25 17:02:44'),
(316, 1, 'Updated user: patobaob@gmail.com', '2025-05-25 17:06:36'),
(317, 1, 'Updated user: patobaob@gmail.com', '2025-05-25 17:06:51'),
(319, 1, 'Logged in', '2025-05-25 17:08:58'),
(320, 1, 'Updated user: patobaob@gmail.com', '2025-05-25 17:09:06'),
(321, 1, 'Logged out', '2025-05-25 17:09:12'),
(322, 2, 'Logged in', '2025-05-25 17:09:26'),
(323, 1, 'Logged in', '2025-05-25 17:09:41'),
(324, 1, 'Updated user: patobaob@gmail.com', '2025-05-25 17:10:26'),
(325, 1, 'Updated Violation: Cheating', '2025-05-25 17:12:52'),
(326, 1, 'Updated Violation: Cheating', '2025-05-25 17:13:21'),
(327, 1, 'Updated Violation: Cheating', '2025-05-25 17:13:29'),
(328, 1, 'Updated Violation: Cheating', '2025-05-25 17:14:16'),
(329, 1, 'Added New Record for Violation ID: 17', '2025-05-25 17:14:50'),
(330, 1, 'Updated Record for Record ID: 19', '2025-05-25 17:17:27'),
(331, 1, 'Added student: Sample Sample', '2025-05-25 17:18:33'),
(332, 1, 'Updated Student: Sample Sample', '2025-05-25 17:19:17'),
(333, 1, 'Deleted user: Sample', '2025-05-25 17:19:23'),
(335, 1, 'Logged in', '2025-05-25 17:20:59'),
(336, 1, 'Added student: Sample Sample', '2025-05-25 17:22:13'),
(337, 1, 'Deleted user: Sample', '2025-05-25 17:23:17'),
(339, 1, 'Logged in', '2025-05-25 17:23:44'),
(340, 1, 'Added student: Xander Parba', '2025-05-25 17:24:16'),
(341, 1, 'Added Violation: Bullying', '2025-05-25 17:25:07'),
(342, 1, 'Updated Violation: Bullyinggg', '2025-05-25 17:25:52'),
(343, 1, 'Deleted Violation: 18', '2025-05-25 17:26:05'),
(344, 1, 'Added Violation: Bullying', '2025-05-25 17:27:04'),
(345, 1, 'Added new user: draymisa@gmail.com', '2025-05-25 17:29:35'),
(346, 1, 'Logged out', '2025-05-25 17:29:37'),
(354, 1, 'Deleted user: draymisa@gmail.com', '2025-05-25 17:33:56'),
(355, 2, 'Logged in', '2025-05-25 21:03:05'),
(356, 2, 'Logged in', '2025-05-25 21:06:31'),
(358, 2, 'Logged in', '2025-05-25 21:09:13'),
(359, 2, 'Logged in', '2025-05-25 21:18:35'),
(360, 2, 'Logged in', '2025-05-25 21:21:34'),
(361, 1, 'Logged in', '2025-05-25 21:27:49'),
(362, 1, 'Logged out', '2025-05-25 21:27:59'),
(363, 2, 'Logged in', '2025-05-25 21:28:12'),
(364, 2, 'Changed password for account: Patricia Ann Obaob', '2025-05-25 21:29:22'),
(365, 2, 'Logged in', '2025-05-25 21:29:35'),
(367, 1, 'Logged in', '2025-05-25 21:37:13'),
(368, 1, 'Changed password for account: Lawrence Sumbi', '2025-05-25 21:37:29'),
(370, 1, 'Logged in', '2025-05-25 21:40:40'),
(371, 1, 'Changed password for account: Lawrence Sumbi', '2025-05-25 21:40:55'),
(372, 1, 'Logged in', '2025-05-25 21:41:17'),
(373, 1, 'Changed password for account: Lawrence Sumbi', '2025-05-25 21:41:38'),
(374, 1, 'Logged in', '2025-05-25 21:41:50'),
(375, 2, 'Logged in', '2025-05-25 21:47:19'),
(376, 2, 'Changed password for account: Patricia Ann Obaob', '2025-05-25 21:48:09'),
(377, 1, 'Logged in', '2025-05-25 21:48:23'),
(378, 2, 'Logged in', '2025-05-25 21:49:06'),
(379, 2, 'Added student: Mary Divine Grace Obaob', '2025-05-25 21:50:09'),
(380, 2, 'Added Violation: Plagiarism', '2025-05-25 21:51:34'),
(381, 2, 'Updated Violation: Plagiarismmmmm', '2025-05-25 21:51:41'),
(382, 2, 'Updated Violation: Plagiarism', '2025-05-25 21:51:47'),
(383, 2, 'Updated Student: Mary Divine Grace Obaob', '2025-05-25 21:53:29'),
(384, 2, 'Updated Violation: Plagiarism', '2025-05-25 21:57:37'),
(385, 2, 'Updated Violation: Plagiarism', '2025-05-25 21:57:46'),
(386, 2, 'Updated Violation: Plagiarism', '2025-05-25 21:58:02'),
(387, 2, 'Logged out', '2025-05-25 21:58:24'),
(388, 2, 'Logged in', '2025-05-25 21:59:17'),
(389, 2, 'Logged out', '2025-05-25 22:02:12'),
(390, 2, 'Logged in', '2025-05-25 22:02:27'),
(391, 2, 'Deleted Violation: 20', '2025-05-25 22:02:34'),
(392, 2, 'Deleted user: Mary Divine Grace', '2025-05-25 22:02:38'),
(393, 2, 'Logged out', '2025-05-25 22:02:49'),
(394, 2, 'Logged in', '2025-05-25 22:03:09'),
(395, 2, 'Logged in', '2025-05-25 22:03:52'),
(396, 2, 'Added student: Mary Divine Grace Obaob', '2025-05-25 22:05:14'),
(397, 2, 'Updated Student: Mary Divine Grace Obaob', '2025-05-25 22:05:36'),
(398, 2, 'Added Violation: Plagiarism', '2025-05-25 22:06:13'),
(399, 2, 'Updated Violation: Plagiarism', '2025-05-25 22:06:24'),
(400, 1, 'Logged in', '2025-05-25 22:07:03'),
(401, 1, 'Added New Record for Violation ID: 21', '2025-05-25 22:08:12'),
(402, 1, 'Logged out', '2025-05-25 22:08:27'),
(403, 1, 'Logged in', '2025-05-25 22:09:14'),
(404, 1, 'Logged in', '2025-05-25 22:13:42'),
(405, 1, 'Logged in', '2025-05-25 22:23:04'),
(406, 1, 'Printed a report from the admin panel.', '2025-05-25 22:24:02'),
(407, 1, 'Logged out', '2025-05-25 22:25:11'),
(408, 1, 'Logged in', '2025-05-25 22:32:15'),
(409, 1, 'Updated Violation: Bullyingg', '2025-05-25 22:32:37'),
(410, 1, 'Logged out', '2025-05-25 22:35:20'),
(411, 1, 'Logged in', '2025-05-25 22:35:30'),
(412, 1, 'Added new user: dray@gmail.com', '2025-05-25 22:36:06'),
(413, 1, 'Logged out', '2025-05-25 22:36:15'),
(414, 58, 'Logged in', '2025-05-25 22:36:25'),
(415, 1, 'Logged in', '2025-05-25 22:46:51'),
(416, 1, 'Logged out', '2025-05-25 22:46:54'),
(417, 2, 'Logged in', '2025-05-25 22:47:04'),
(418, 58, 'Logged in', '2025-05-25 22:47:26'),
(419, 2, 'Logged in', '2025-05-25 22:50:08'),
(420, 1, 'Logged in', '2025-05-25 22:52:08'),
(421, 2, 'Logged in', '2025-05-25 22:53:18'),
(422, 2, 'Logged in', '2025-05-25 22:57:27'),
(423, 2, 'Logged in', '2025-05-25 23:01:04'),
(424, 2, 'Logged in', '2025-05-25 23:09:51'),
(425, 2, 'Added student: King James Obaob', '2025-05-25 23:11:52'),
(426, 2, 'Added Violation: Lying', '2025-05-25 23:13:51'),
(427, 1, 'Logged in', '2025-05-25 23:14:24'),
(429, 2, 'Logged in', '2025-05-25 23:31:50'),
(430, 2, 'Logged out', '2025-05-25 23:32:26'),
(431, 1, 'Logged in', '2025-05-25 23:32:34'),
(432, 1, 'Updated user: jaylon@gmail.com', '2025-05-25 23:34:31'),
(433, 1, 'Updated user: jaylon@gmail.com', '2025-05-25 23:34:42'),
(434, 1, 'Logged out', '2025-05-25 23:36:13'),
(435, 1, 'Logged in', '2025-05-25 23:36:20'),
(437, 1, 'Logged in', '2025-05-25 23:40:48'),
(438, 1, 'Logged in', '2025-05-25 23:47:15'),
(439, 1, 'Logged out', '2025-05-25 23:47:29'),
(440, 2, 'Logged in', '2025-05-25 23:47:36'),
(441, 2, 'Logged out', '2025-05-25 23:48:41'),
(442, 1, 'Logged in', '2025-05-26 00:16:03'),
(443, 1, 'Logged out', '2025-05-26 00:16:05'),
(444, 2, 'Logged in', '2025-05-26 00:18:09'),
(445, 2, 'Logged out', '2025-05-26 00:18:11'),
(446, 1, 'Logged in', '2025-05-26 00:21:12'),
(447, 1, 'Logged out', '2025-05-26 00:21:17'),
(448, 1, 'Logged in', '2025-05-26 00:53:33');

-- --------------------------------------------------------

--
-- Table structure for table `rec_table`
--

CREATE TABLE `rec_table` (
  `rec_id` int(11) NOT NULL,
  `vio_id` int(11) NOT NULL,
  `rec_sanction` varchar(50) NOT NULL,
  `rec_comment` varchar(50) NOT NULL,
  `rec_stamp` varchar(50) NOT NULL,
  `user_id` int(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rec_table`
--

INSERT INTO `rec_table` (`rec_id`, `vio_id`, `rec_sanction`, `rec_comment`, `rec_stamp`, `user_id`) VALUES
(19, 17, 'Zero in Examm', 'Do not do it again', '25/05/25 05:17 PM', 1),
(20, 21, 'Zero Score in Activity', 'Do not copy someone\'s work', '25/05/25 10:08 PM', 1);

-- --------------------------------------------------------

--
-- Table structure for table `stud_table`
--

CREATE TABLE `stud_table` (
  `stud_id` int(50) NOT NULL,
  `stud_fname` varchar(50) NOT NULL,
  `stud_lname` varchar(50) NOT NULL,
  `stud_program` varchar(50) NOT NULL,
  `stud_section` varchar(50) NOT NULL,
  `stud_address` varchar(50) NOT NULL,
  `stud_cnumber` varchar(50) NOT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `user_id` int(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stud_table`
--

INSERT INTO `stud_table` (`stud_id`, `stud_fname`, `stud_lname`, `stud_program`, `stud_section`, `stud_address`, `stud_cnumber`, `image_path`, `user_id`) VALUES
(54, 'Bryll Josh', 'Parba', 'BSIT', '1C', 'Minglanilla', '09123456789', 'src/studentImagesBryll Josh_Parba.jpg', 1),
(57, 'Xander', 'Parba', 'BSED', '1A', 'Minglanilla', '09123456789', NULL, 1),
(59, 'Mary Divine Grace', 'Obaob', 'BSED', '1A', 'San Fernando', '09123456789', 'src/studentImagesMary Divine Grace_Obaob.jpg', 2),
(60, 'King James', 'Obaob', 'BSED', '1A', 'San Fernando', '09123456789', NULL, 2);

-- --------------------------------------------------------

--
-- Table structure for table `user_table`
--

CREATE TABLE `user_table` (
  `user_id` int(50) NOT NULL,
  `user_fname` varchar(50) NOT NULL,
  `user_cnumber` varchar(50) NOT NULL,
  `user_email` varchar(50) NOT NULL,
  `user_password` varchar(150) NOT NULL,
  `user_type` varchar(50) NOT NULL,
  `user_status` varchar(50) NOT NULL,
  `image_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_table`
--

INSERT INTO `user_table` (`user_id`, `user_fname`, `user_cnumber`, `user_email`, `user_password`, `user_type`, `user_status`, `image_path`) VALUES
(1, 'Lawrence Sumbi', '09303172724', 'guiansumbi@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'Admin', 'Active', 'src/usersImagesguiansumbi@gmail.com.jpg'),
(2, 'Patricia Ann Obaob', '09059641855', 'patobaob@gmail.com', 'a7d579ba76398070eae654c30ff153a4c273272a', 'User', 'Active', 'src/usersImagespatobaob@gmail.com.jpg'),
(57, 'Jaylon Mantillas', '09123456789', 'jaylon@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Pending', NULL),
(58, 'Dranreb Misa', '0912345678', 'dray@gmail.com', 'cbfdac6008f9cab4083784cbd1874f76618d2a97', 'User', 'Active', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `vio_table`
--

CREATE TABLE `vio_table` (
  `vio_id` int(50) NOT NULL,
  `stud_id` int(50) NOT NULL,
  `vio_name` varchar(50) NOT NULL,
  `vio_des` varchar(50) NOT NULL,
  `vio_sev` varchar(50) NOT NULL,
  `vio_stamp` varchar(50) NOT NULL,
  `image_path` varchar(255) DEFAULT NULL,
  `vio_status` varchar(50) NOT NULL,
  `user_id` int(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vio_table`
--

INSERT INTO `vio_table` (`vio_id`, `stud_id`, `vio_name`, `vio_des`, `vio_sev`, `vio_stamp`, `image_path`, `vio_status`, `user_id`) VALUES
(17, 54, 'Cheating', 'Bringing CheatSheet in Exam', 'Low', '25/05/25 05:14 PM', 'src/violationImages17.jpg', 'Recorded', 1),
(19, 57, 'Bullyingg', 'Saying \"Bayot\" to his cm', 'Minor', '25/05/25 10:32 PM', NULL, 'Pending', 1),
(21, 59, 'Plagiarism', 'Copying of others work', 'Minor', '25/05/25 10:06 PM', 'src/violationImages21.jpg', 'Recorded', 2),
(22, 60, 'Lying', 'Provinding False Information', 'Minor', '25/05/25 11:13 PM', NULL, 'Pending', 2);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `logs_table`
--
ALTER TABLE `logs_table`
  ADD PRIMARY KEY (`logs_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `rec_table`
--
ALTER TABLE `rec_table`
  ADD PRIMARY KEY (`rec_id`),
  ADD KEY `vio_id` (`vio_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `stud_table`
--
ALTER TABLE `stud_table`
  ADD PRIMARY KEY (`stud_id`),
  ADD KEY `user_id` (`user_id`);

--
-- Indexes for table `user_table`
--
ALTER TABLE `user_table`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `vio_table`
--
ALTER TABLE `vio_table`
  ADD PRIMARY KEY (`vio_id`),
  ADD KEY `stud_id` (`stud_id`),
  ADD KEY `user_id` (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `logs_table`
--
ALTER TABLE `logs_table`
  MODIFY `logs_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=449;

--
-- AUTO_INCREMENT for table `rec_table`
--
ALTER TABLE `rec_table`
  MODIFY `rec_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- AUTO_INCREMENT for table `stud_table`
--
ALTER TABLE `stud_table`
  MODIFY `stud_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=61;

--
-- AUTO_INCREMENT for table `user_table`
--
ALTER TABLE `user_table`
  MODIFY `user_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;

--
-- AUTO_INCREMENT for table `vio_table`
--
ALTER TABLE `vio_table`
  MODIFY `vio_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `logs_table`
--
ALTER TABLE `logs_table`
  ADD CONSTRAINT `logs_table_user_id_fr` FOREIGN KEY (`user_id`) REFERENCES `user_table` (`user_id`);

--
-- Constraints for table `rec_table`
--
ALTER TABLE `rec_table`
  ADD CONSTRAINT `rec_table_user_id_fr` FOREIGN KEY (`user_id`) REFERENCES `user_table` (`user_id`),
  ADD CONSTRAINT `rec_table_vio_id_fr` FOREIGN KEY (`vio_id`) REFERENCES `vio_table` (`vio_id`);

--
-- Constraints for table `stud_table`
--
ALTER TABLE `stud_table`
  ADD CONSTRAINT `stud_table_user_id_fr` FOREIGN KEY (`user_id`) REFERENCES `user_table` (`user_id`);

--
-- Constraints for table `vio_table`
--
ALTER TABLE `vio_table`
  ADD CONSTRAINT `vio_table_stud_id_fr` FOREIGN KEY (`stud_id`) REFERENCES `stud_table` (`stud_id`),
  ADD CONSTRAINT `vio_table_user_id_fr` FOREIGN KEY (`user_id`) REFERENCES `user_table` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
