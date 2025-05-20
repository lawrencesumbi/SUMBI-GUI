-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 20, 2025 at 04:55 PM
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
(7, 1, 'Logged in', '2025-04-01 21:46:53'),
(8, 1, 'Logged in', '2025-04-01 22:26:22'),
(10, 1, 'Logged in', '2025-04-01 22:46:25'),
(11, 1, 'Logged in', '2025-04-01 22:53:00'),
(12, 1, 'Logged in', '2025-04-01 23:12:31'),
(13, 1, 'Logged in', '2025-04-01 23:21:22'),
(14, 1, 'Logged out', '2025-04-01 23:21:28'),
(15, 16, 'Logged in', '2025-04-01 23:22:57'),
(16, 16, 'Logged out', '2025-04-01 23:23:02'),
(17, 1, 'Logged in', '2025-04-01 23:28:04'),
(20, 1, 'Logged in', '2025-04-22 20:25:01'),
(21, 1, 'Updated Student: Xander Parba', '2025-04-22 20:25:32'),
(23, 1, 'Logged in', '2025-04-22 20:29:46'),
(24, 1, 'Updated user: dyubli@gmail.com', '2025-04-22 20:32:47'),
(26, 1, 'Logged in', '2025-04-22 20:45:41'),
(29, 1, 'Logged in', '2025-04-22 20:53:39'),
(30, 1, 'Added student: Rexon Sumbi', '2025-04-22 20:54:16'),
(32, 1, 'Logged in', '2025-04-22 20:55:14'),
(33, 1, 'Logged in', '2025-04-22 20:57:43'),
(34, 1, 'Logged in', '2025-04-22 21:01:18'),
(35, 1, 'Logged in', '2025-04-22 21:02:42'),
(36, 1, 'Logged in', '2025-04-22 21:04:50'),
(37, 1, 'Logged in', '2025-04-22 21:06:43'),
(38, 2, 'Logged in', '2025-04-22 21:10:30'),
(39, 1, 'Logged in', '2025-04-22 21:10:53'),
(40, 1, 'Logged out', '2025-04-22 21:10:55'),
(41, 1, 'Logged in', '2025-04-22 21:11:21'),
(42, 1, 'Logged out', '2025-04-22 21:11:25'),
(43, 1, 'Logged in', '2025-04-22 21:11:36'),
(48, 1, 'Logged in', '2025-04-22 22:33:40'),
(55, 1, 'Logged in', '2025-04-23 01:18:57'),
(56, 1, 'Printed a report from the admin panel.', '2025-04-23 01:19:22'),
(58, 1, 'Logged in', '2025-04-23 01:33:27'),
(61, 1, 'Logged in', '2025-04-23 01:47:19'),
(62, 1, 'Logged out', '2025-04-23 01:47:50'),
(64, 1, 'Logged in', '2025-04-23 01:48:33'),
(66, 1, 'Logged in', '2025-04-23 01:52:39'),
(67, 1, 'Added New Record', '2025-04-23 01:53:19'),
(69, 1, 'Logged in', '2025-04-23 02:03:43'),
(70, 1, 'Updated Record for Record ID: 10', '2025-04-23 02:03:55'),
(72, 1, 'Logged in', '2025-04-23 02:06:25'),
(74, 1, 'Logged in', '2025-04-23 02:14:41'),
(76, 1, 'Logged in', '2025-04-23 02:15:55'),
(78, 1, 'Logged in', '2025-04-23 02:17:39'),
(80, 1, 'Logged in', '2025-04-23 02:18:29'),
(82, 1, 'Logged in', '2025-04-23 02:20:14'),
(84, 1, 'Logged in', '2025-04-23 02:22:00'),
(85, 1, 'Logged out', '2025-04-23 02:23:12'),
(87, 1, 'Logged in', '2025-04-23 02:28:31'),
(89, 1, 'Logged in', '2025-04-23 02:29:48'),
(91, 1, 'Logged in', '2025-04-23 02:31:05'),
(93, 1, 'Logged in', '2025-04-23 02:36:11'),
(95, 1, 'Logged in', '2025-04-23 02:38:07'),
(97, 1, 'Logged in', '2025-04-23 02:40:00'),
(98, 1, 'Logged in', '2025-05-20 08:42:52'),
(101, 1, 'Logged in', '2025-05-20 08:48:58'),
(102, 1, 'Logged out', '2025-05-20 08:53:01'),
(103, 1, 'Logged in', '2025-05-20 08:53:34'),
(104, 1, 'Logged in', '2025-05-20 08:58:09'),
(105, 1, 'Logged in', '2025-05-20 09:08:36'),
(106, 1, 'Logged in', '2025-05-20 09:12:22'),
(107, 1, 'Logged out', '2025-05-20 09:13:45'),
(108, 2, 'Logged in', '2025-05-20 09:19:24'),
(109, 1, 'Logged in', '2025-05-20 09:22:17'),
(110, 1, 'Logged in', '2025-05-20 09:36:12'),
(111, 1, 'Logged out', '2025-05-20 09:36:20'),
(112, 2, 'Logged in', '2025-05-20 09:36:27'),
(113, 1, 'Logged in', '2025-05-20 09:37:01'),
(114, 1, 'Logged out', '2025-05-20 09:53:20'),
(115, 2, 'Logged in', '2025-05-20 09:53:34'),
(116, 2, 'Logged in', '2025-05-20 09:58:31'),
(117, 1, 'Logged in', '2025-05-20 10:01:44'),
(118, 1, 'Logged out', '2025-05-20 10:01:49'),
(119, 2, 'Logged in', '2025-05-20 10:02:02'),
(120, 2, 'Logged in', '2025-05-20 10:07:51'),
(121, 1, 'Logged in', '2025-05-20 11:49:19'),
(122, 1, 'Updated Student: Xander Parba', '2025-05-20 11:51:48'),
(123, 1, 'Deleted user: Rexon', '2025-05-20 11:52:11'),
(124, 1, 'Added student: Dray Misa', '2025-05-20 11:52:19'),
(125, 1, 'Deleted user: Dray', '2025-05-20 11:52:38'),
(126, 1, 'Added student: Dray Misa', '2025-05-20 11:53:21'),
(127, 1, 'Updated Student: Dray Misa', '2025-05-20 11:54:48'),
(128, 1, 'Logged in', '2025-05-20 11:57:11'),
(129, 1, 'Logged in', '2025-05-20 12:05:49'),
(130, 1, 'Logged in', '2025-05-20 12:07:08'),
(131, 1, 'Logged in', '2025-05-20 12:11:13'),
(132, 1, 'Logged in', '2025-05-20 12:12:05'),
(133, 1, 'Logged in', '2025-05-20 12:13:38'),
(134, 1, 'Logged in', '2025-05-20 12:14:07'),
(135, 1, 'Logged in', '2025-05-20 12:23:58'),
(137, 1, 'Logged in', '2025-05-20 19:35:44'),
(139, 1, 'Logged in', '2025-05-20 19:39:15'),
(141, 1, 'Logged in', '2025-05-20 19:40:14'),
(150, 1, 'Logged in', '2025-05-20 21:25:42'),
(151, 1, 'Deleted user: test2@gmail.com', '2025-05-20 21:26:01'),
(152, 1, 'Deleted user: test11@gmail.com', '2025-05-20 21:26:06'),
(153, 1, 'Logged in', '2025-05-20 21:28:19'),
(154, 1, 'Deleted Violation: 15', '2025-05-20 21:29:24'),
(155, 1, 'Logged in', '2025-05-20 21:34:26'),
(157, 1, 'Logged in', '2025-05-20 22:06:19'),
(158, 1, 'Added new user: test25@gmail.com', '2025-05-20 22:07:35'),
(159, 1, 'Logged out', '2025-05-20 22:07:40'),
(160, 1, 'Logged in', '2025-05-20 22:08:06'),
(161, 1, 'Updated user: test25@gmail.com', '2025-05-20 22:08:16'),
(162, 1, 'Logged out', '2025-05-20 22:08:19'),
(163, 1, 'Logged in', '2025-05-20 22:08:32'),
(164, 1, 'Logged out', '2025-05-20 22:08:50'),
(165, 53, 'Logged in', '2025-05-20 22:09:02'),
(167, 1, 'Logged in', '2025-05-20 22:10:06'),
(168, 1, 'Added new user: test26@gmail.com', '2025-05-20 22:10:32'),
(170, 1, 'Logged in', '2025-05-20 22:16:24'),
(171, 1, 'Logged in', '2025-05-20 22:17:34'),
(172, 1, 'Logged in', '2025-05-20 22:19:13'),
(173, 1, 'Logged in', '2025-05-20 22:23:11'),
(174, 1, 'Logged in', '2025-05-20 22:24:21'),
(175, 1, 'Logged in', '2025-05-20 22:33:16'),
(176, 1, 'Logged in', '2025-05-20 22:38:26'),
(178, 1, 'Logged in', '2025-05-20 22:46:44'),
(180, 1, 'Logged in', '2025-05-20 22:53:41');

-- --------------------------------------------------------

--
-- Table structure for table `rec_table`
--

CREATE TABLE `rec_table` (
  `rec_id` int(11) NOT NULL,
  `vio_id` int(11) NOT NULL,
  `rec_sanction` varchar(50) NOT NULL,
  `rec_comment` varchar(50) NOT NULL,
  `rec_stamp` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `rec_table`
--

INSERT INTO `rec_table` (`rec_id`, `vio_id`, `rec_sanction`, `rec_comment`, `rec_stamp`) VALUES
(8, 2, 'test', 'test', '25/04/01 12:41 AM'),
(9, 10, 'Suspension for 3 days', 'Do not do it again', '25/04/01 01:33 AM'),
(10, 6, 'Minus 20 points', 'Make it earlier', '25/04/23 01:53 AM');

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
  `image_path` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `stud_table`
--

INSERT INTO `stud_table` (`stud_id`, `stud_fname`, `stud_lname`, `stud_program`, `stud_section`, `stud_address`, `stud_cnumber`, `image_path`) VALUES
(1, 'Xander', 'Parba', 'BSIT', '3A', 'Vito Minglanilla', '09123456789', 'src/studentImagesXander_Parba.jpg'),
(2, 'Xena Reika', 'Pader', 'BSED', '2A', 'Tulay Minglanilla', '09123456789', ''),
(4, 'Daniel', 'Padilla', 'BSCRIM', '4C', 'Cebu City', '09123456789', ''),
(5, 'Lawrence', 'Sumbi', 'BSIT', '2A', 'Minglanilla', '09303172724', 'src/studentImagesLawrence_Sumbi.jpg'),
(6, 'Patricia Ann Mae', 'Obaob', 'BSIT', '2A', 'San Fernando', '09753140724', ''),
(7, 'Bryll Josh', 'Parba', 'BSIT', '1A', 'Minglanilla', '09123456789', ''),
(8, 'John Reyl', 'Saragosa', 'BSIT', '2A', 'Naga', '09123456789', ''),
(9, 'Ronald', 'Rosales', 'BSIT', '1A', 'Toledo', '09123456789', ''),
(10, 'Benish Reyna', 'Parba', 'BSED', '1A', 'Minglanilla', '09123456789', ''),
(11, 'Michael', 'Bustamante', 'BSIT', '4A', 'Minglanilla', '09123456789', ''),
(51, 'Dray', 'Misa', 'BSIT', '2A', 'Naga', '09123456789', 'src/studentImagesDray_Misa.jpg');

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
(2, 'Patricia Obaob', '09059641855', 'patobaob@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Active', 'src/usersImagespatobaob@gmail.com.jpg'),
(4, 'Diovely Campo', '09876543210', 'dyubli@gmail.com', 'a7d579ba76398070eae654c30ff153a4c273272a', 'User', 'Active', 'src/usersImagesdyubli@gmail.com.jpg'),
(8, 'Sample2', '09123456789', 'sample2@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Active', '0'),
(9, 'Sample3', '09123456789', 'sample3@gmal.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Pending', '0'),
(14, 'Sample10', '09123465789', 'sample10@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Pending', '0'),
(16, 'Dalley Alterado', '09123456789', 'dalleyalterado@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'Admin', 'Active', '0'),
(21, 'Lawrence Sumbi', '09753140724', 'sample3@gmail.com', 'b720f1e4dd394eb5b9a69890f704da48c5677af0', 'Admin', 'Active', '0'),
(23, 'chuchu', '09123456789', 'chuchu@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Active', '0'),
(24, 'chichi', '09123456789', 'chichi@scc.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Pending', '0'),
(25, 'test', '09123456788', 'test@scc.com', 'a94a8fe5ccb19ba61c4c0873d391e987982fbbd3', 'User', 'Active', 'src/usersImagestest@scc.com.jpg'),
(43, 'test15', '09123456789', 'test15@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Active', NULL),
(46, 'test18', '09123456789', 'test18@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Active', NULL),
(47, 'test19', '09123456789', 'test19@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Active', NULL),
(48, 'test20', '09123456789', 'test20@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Active', NULL),
(49, 'test21', '09123456789', 'test21@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Active', NULL),
(50, 'test22', '09123456789', 'test22@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Active', 'src/usersImagestest22@gmail.com.jpg'),
(51, 'test23', '09123456789', 'test23@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Active', 'src/usersImagestest23@gmail.com.jpg'),
(52, 'test24', '09123456789', 'test24@gmail.com', '7c222fb2927d828af22f592134e8932480637c0d', 'User', 'Active', NULL),
(53, 'TEST25', '09123456789', 'test25@gmail.com', '439f824422de32bb154e0e584ed61cf9ea519024', 'User', 'Active', NULL),
(54, 'test26', '09123456789', 'test26@gmail.com', 'cbfdac6008f9cab4083784cbd1874f76618d2a97', 'Admin', 'Active', NULL);

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
  `vio_status` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `vio_table`
--

INSERT INTO `vio_table` (`vio_id`, `stud_id`, `vio_name`, `vio_des`, `vio_sev`, `vio_stamp`, `image_path`, `vio_status`) VALUES
(2, 1, 'Vandalisms', 'test12', 'test12', '25/03/31 10:27 PM', 'src/violationImages2.jpg', 'Recorded'),
(5, 2, 'Bullying', 'Sample', 'sample', '25/03/31 08:47 PM', '', 'Pending'),
(6, 4, 'Late Submission', 'sample', 'sample', '25/03/31 08:48 PM', '', 'Recorded'),
(10, 11, 'Cheating', 'Harsh Words', 'Medium', '25/03/31 07:22 PM', 'src/violationImages10.jpg', 'Recorded'),
(11, 9, 'Sleeping', 'sample', 'sample', '25/03/31 08:48 PM', NULL, 'Pending'),
(13, 4, 'Test', 'testestest', 'high', '25/03/31 09:24 PM', NULL, 'Pending'),
(14, 1, 'samples1', 'samples', 'samples', '25/03/31 10:27 PM', 'src/violationImages14.jpg', 'Pending');

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
  ADD KEY `vio_id` (`vio_id`);

--
-- Indexes for table `stud_table`
--
ALTER TABLE `stud_table`
  ADD PRIMARY KEY (`stud_id`);

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
  ADD KEY `stud_id` (`stud_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `logs_table`
--
ALTER TABLE `logs_table`
  MODIFY `logs_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=181;

--
-- AUTO_INCREMENT for table `rec_table`
--
ALTER TABLE `rec_table`
  MODIFY `rec_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `stud_table`
--
ALTER TABLE `stud_table`
  MODIFY `stud_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=52;

--
-- AUTO_INCREMENT for table `user_table`
--
ALTER TABLE `user_table`
  MODIFY `user_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;

--
-- AUTO_INCREMENT for table `vio_table`
--
ALTER TABLE `vio_table`
  MODIFY `vio_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

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
  ADD CONSTRAINT `rec_table_vio_id_fr` FOREIGN KEY (`vio_id`) REFERENCES `vio_table` (`vio_id`);

--
-- Constraints for table `vio_table`
--
ALTER TABLE `vio_table`
  ADD CONSTRAINT `vio_table_stud_id_fr` FOREIGN KEY (`stud_id`) REFERENCES `stud_table` (`stud_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
