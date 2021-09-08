-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 13, 2021 at 03:05 PM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 7.3.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `lostitems_db`
--

-- --------------------------------------------------------

--
-- Table structure for table `barang`
--

CREATE TABLE `barang` (
  `id` int(11) NOT NULL,
  `nama` varchar(30) CHARACTER SET utf8mb4 NOT NULL,
  `jenis` varchar(20) CHARACTER SET utf8mb4 NOT NULL,
  `ditemukan` varchar(10) CHARACTER SET utf8mb4 NOT NULL,
  `keterangan` varchar(60) CHARACTER SET utf8mb4 NOT NULL,
  `status` varchar(20) CHARACTER SET utf8mb4 NOT NULL,
  `tgl_ditemukan` date DEFAULT NULL,
  `picture` varchar(100) CHARACTER SET utf8mb4 NOT NULL DEFAULT '/lost_items/penyimpanan/insert_here.jpeg'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `barang`
--

INSERT INTO `barang` (`id`, `nama`, `jenis`, `ditemukan`, `keterangan`, `status`, `tgl_ditemukan`, `picture`) VALUES
(52, 'Mouse Logitech M331', 'Elektronik', 'Lab 8', 'Warna merah, silent, wireless, usb tidak ada', 'Belum Dikembalikan', '2021-06-08', '/lost_items/penyimpanan/52.jpeg');

--
-- Triggers `barang`
--
DELIMITER $$
CREATE TRIGGER `delete_barang` AFTER DELETE ON `barang` FOR EACH ROW BEGIN 
INSERT INTO log_barang (id, nama, jenis, ditemukan, keterangan, status, tgl_ditemukan, picture) VALUES
(OLD.id, OLD.nama, OLD.jenis, OLD.ditemukan, OLD.keterangan, OLD.status, OLD.tgl_ditemukan, OLD.picture);
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `log_barang`
--

CREATE TABLE `log_barang` (
  `id` int(3) NOT NULL,
  `nama` varchar(30) NOT NULL,
  `jenis` varchar(20) NOT NULL,
  `ditemukan` varchar(10) NOT NULL,
  `keterangan` varchar(50) NOT NULL,
  `status` varchar(20) NOT NULL,
  `tgl_ditemukan` date NOT NULL,
  `picture` varchar(60) NOT NULL DEFAULT '/lost_items/penyimpanan/insert_here.jpeg'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `log_barang`
--

INSERT INTO `log_barang` (`id`, `nama`, `jenis`, `ditemukan`, `keterangan`, `status`, `tgl_ditemukan`, `picture`) VALUES
(57, 'Lenovo Ideapad 330s', 'Elektronik', 'Lab 4', 'Ukuran 14\", terdapat stiker filosofi kopi', 'Belum Dikembalikan', '2021-06-07', '/lost_items/penyimpanan/57.jpeg');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `username` varchar(32) NOT NULL,
  `name` varchar(32) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `name`, `email`, `password`) VALUES
(1, 'agungmrf_', 'Agung Maruf', '1911500518@student.budiluhur.ac.id', '202cb962ac59075b964b07152d234b70'),
(2, 'agstnmira', 'Mira Agustina', 'mir@gmail.com', '827ccb0eea8a706c4c34a16891f84e7b');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `barang`
--
ALTER TABLE `barang`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `log_barang`
--
ALTER TABLE `log_barang`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `barang`
--
ALTER TABLE `barang`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;

--
-- AUTO_INCREMENT for table `log_barang`
--
ALTER TABLE `log_barang`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=58;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
