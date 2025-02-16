-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Feb 16, 2025 at 03:50 PM
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
-- Table structure for table `user_table`
--

CREATE TABLE `user_table` (
  `user_id` int(50) NOT NULL,
  `user_fname` varchar(50) NOT NULL,
  `user_cnumber` varchar(50) NOT NULL,
  `user_email` varchar(50) NOT NULL,
  `user_password` varchar(50) NOT NULL,
  `user_type` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `user_table`
--

INSERT INTO `user_table` (`user_id`, `user_fname`, `user_cnumber`, `user_email`, `user_password`, `user_type`) VALUES
(1, 'Lawrence Sumbi', '09753140724', 'guiansumbi@gmail.com', '12345678', 'User'),
(2, 'Patricia Obaob', '09059641855', 'patobaob@gmail.com', '12345678', 'User'),
(3, 'Mike Bustamante', '09123456789', 'mikebustamante@gmail.com', '87654321', 'Admin'),
(4, 'Diovely Campo', '09876543210', 'dyubli@gmail.com', '12345678', 'User'),
(5, 'Sample', '12345678910', 'sample@gmail.com', '1234567', 'User'),
(6, 'Sample1', '12345678910', 'sample1@gmail.com', '12345678', 'User'),
(7, 'Guian Sumbi', '09303172724', 'sumbiguian@gmail.com', '12345678', 'User'),
(8, 'Sample2', '09123456789', 'sample2@gmail.com', '12345678', 'User'),
(9, 'Sample3', '09123456789', 'sample3@gmal.com', '12345678', 'User');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `user_table`
--
ALTER TABLE `user_table`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `user_table`
--
ALTER TABLE `user_table`
  MODIFY `user_id` int(50) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
