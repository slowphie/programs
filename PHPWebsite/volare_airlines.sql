-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: Oct 15, 2017 at 02:21 AM
-- Server version: 10.1.25-MariaDB
-- PHP Version: 7.0.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `volare_airlines`
--

-- --------------------------------------------------------

--
-- Table structure for table `age`
--

CREATE TABLE `age` (
  `age_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `age`
--

INSERT INTO `age` (`age_id`, `name`) VALUES
(1, 'Adult'),
(2, 'Child');

-- --------------------------------------------------------

--
-- Table structure for table `billing_details`
--

CREATE TABLE `billing_details` (
  `id` int(11) NOT NULL,
  `given_name` varchar(100) NOT NULL,
  `surname` varchar(100) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `email` varchar(100) NOT NULL,
  `price` int(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `class_price`
--

CREATE TABLE `class_price` (
  `class_id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `adult_price` int(4) NOT NULL,
  `child_price` int(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `class_price`
--

INSERT INTO `class_price` (`class_id`, `name`, `adult_price`, `child_price`) VALUES
(1, 'First', 3850, 2880),
(2, 'Business', 1925, 1440),
(3, 'Economy', 960, 720);

-- --------------------------------------------------------

--
-- Table structure for table `passenger`
--

CREATE TABLE `passenger` (
  `id_passenger` int(11) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `first_name` varchar(80) NOT NULL,
  `date_of_birth` date NOT NULL,
  `adult_child` int(2) NOT NULL,
  `seat_number` varchar(4) NOT NULL,
  `class` int(2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `seating`
--

CREATE TABLE `seating` (
  `id` int(3) NOT NULL,
  `seat` varchar(4) NOT NULL,
  `class` int(2) NOT NULL,
  `available` tinyint(1) NOT NULL,
  `restricted` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `seating`
--

INSERT INTO `seating` (`id`, `seat`, `class`, `available`, `restricted`) VALUES
(1, '1a', 1, 1, 0),
(2, '1b', 1, 1, 0),
(3, '2a', 1, 1, 0),
(4, '2b', 1, 1, 0),
(5, '3a', 1, 1, 0),
(6, '3b', 1, 1, 0),
(7, '4a', 2, 1, 0),
(8, '4b', 2, 1, 0),
(9, '4c', 2, 1, 0),
(10, '4d', 2, 1, 0),
(11, '5a', 2, 1, 0),
(12, '5b', 2, 1, 0),
(13, '5c', 2, 1, 0),
(14, '5d', 2, 1, 0),
(15, '6a', 2, 1, 0),
(16, '6b', 2, 1, 0),
(17, '6c', 2, 1, 0),
(18, '6d', 2, 1, 0),
(19, '7a', 3, 1, 1),
(20, '7b', 3, 1, 1),
(21, '7c', 3, 1, 1),
(22, '7d', 3, 1, 1),
(23, '8a', 3, 1, 0),
(24, '8b', 3, 1, 0),
(25, '8c', 3, 1, 0),
(26, '8d', 3, 1, 0),
(27, '9a', 3, 1, 0),
(28, '9b', 3, 1, 0),
(29, '9c', 3, 1, 0),
(30, '9d', 3, 1, 0),
(31, '10a', 3, 1, 0),
(32, '10b', 3, 1, 0),
(33, '10c', 3, 1, 0),
(34, '10d', 3, 1, 0),
(35, '11a', 3, 1, 0),
(36, '11b', 3, 1, 0),
(37, '11c', 3, 1, 0),
(38, '11d', 3, 1, 0),
(39, '12a', 3, 1, 0),
(40, '12b', 3, 1, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `age`
--
ALTER TABLE `age`
  ADD PRIMARY KEY (`age_id`);

--
-- Indexes for table `billing_details`
--
ALTER TABLE `billing_details`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `class_price`
--
ALTER TABLE `class_price`
  ADD PRIMARY KEY (`class_id`);

--
-- Indexes for table `passenger`
--
ALTER TABLE `passenger`
  ADD PRIMARY KEY (`id_passenger`);

--
-- Indexes for table `seating`
--
ALTER TABLE `seating`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `age`
--
ALTER TABLE `age`
  MODIFY `age_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `billing_details`
--
ALTER TABLE `billing_details`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `class_price`
--
ALTER TABLE `class_price`
  MODIFY `class_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT for table `passenger`
--
ALTER TABLE `passenger`
  MODIFY `id_passenger` int(11) NOT NULL AUTO_INCREMENT;COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
