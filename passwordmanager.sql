-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 23, 2025 at 07:08 PM
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
-- Database: `passwordmanager`
--

-- --------------------------------------------------------

--
-- Table structure for table `useraccounts`
--

CREATE TABLE `useraccounts` (
  `AccountID` int(11) NOT NULL,
  `UserID` int(11) DEFAULT NULL,
  `AccountName` varchar(100) NOT NULL,
  `AccountUserName` varchar(100) NOT NULL,
  `AccountPassword` varchar(255) NOT NULL,
  `size` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `useraccounts`
--

INSERT INTO `useraccounts` (`AccountID`, `UserID`, `AccountName`, `AccountUserName`, `AccountPassword`, `size`) VALUES
(1, 1, 'Facebook', 'manar_fb', '$Zg\'c+|k)=[x?9z)pO', 17),
(2, 1, 'Twitter', 'manar_twitter', '$Zg\'c+|k)=[x?9=UK4l>B#', 22),
(3, 1, 'Instagram', 'manar_instagram', '$Zg\'c+|k)=[x?98:W{+dF%)u', 24),
(4, 1, 'LinkedIn', 'manar_linkedin', '$Zg\'c+|k)=[x?9&~\"5^g$lH', 23),
(5, 2, 'Facebook', 'lama_fb', '$Zg\'c+|k&~)u,5xS', 16),
(6, 2, 'Twitter', 'lama_twitter', '$Zg\'c+|k&~)u,5xS', 21),
(7, 2, 'Instagram', 'lama_instagram', '$Zg\'c+|k&~)u,5\"5j<2c*bC6', 23),
(8, 2, 'LinkedIn', 'lama_linkedin', '$Zg\'c+|k&~)u,5\'t\\11X\"5', 22),
(9, 3, 'Facebook', 'lana_fb', '$Zg\'c+|k&~[x,5xS', 16),
(10, 3, 'Twitter', 'lana_twitter', '$Zg\'c+|k&~[x,5uBP[>pb}', 21),
(11, 3, 'Instagram', 'lana_instagram', '$Zg\'c+|k&~[x,5\"5j<2c*bC6', 23),
(12, 3, 'LinkedIn', 'lana_linkedin', '$Zg\'c+|k&~[x,5\'t\\11X\"5', 22),
(13, 4, 'Facebook', 'sama_fb', '$Zg\'c+|k~R)u,5xS', 16),
(14, 4, 'Twitter', 'sama_twitter', '$Zg\'c+|k~R)u,5uBP[>pb}', 21),
(15, 4, 'Instagram', 'sama_instagram', '$Zg\'c+|k~R)u,5\"5j<2c*bC6', 23),
(16, 4, 'LinkedIn', 'sama_linkedin', '$Zg\'c+|k~R)u,5\'t\\11X\"5', 22);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `UserID` int(11) NOT NULL,
  `UserName` varchar(100) NOT NULL,
  `UserEmail` varchar(100) NOT NULL,
  `UserPassword` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`UserID`, `UserName`, `UserEmail`, `UserPassword`) VALUES
(1, 'Manar', 'manar@example.com', 'password_manar'),
(2, 'Lama', 'lama@example.com', 'password_lama'),
(3, 'Lana', 'lana@example.com', 'password_lana'),
(4, 'Sama', 'sama@example.com', 'password_sama');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `useraccounts`
--
ALTER TABLE `useraccounts`
  ADD PRIMARY KEY (`AccountID`),
  ADD KEY `UserID` (`UserID`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`UserID`),
  ADD UNIQUE KEY `UserEmail` (`UserEmail`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `useraccounts`
--
ALTER TABLE `useraccounts`
  MODIFY `AccountID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=29;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `UserID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `useraccounts`
--
ALTER TABLE `useraccounts`
  ADD CONSTRAINT `useraccounts_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
