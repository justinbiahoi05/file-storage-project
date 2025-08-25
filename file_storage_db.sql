-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th8 25, 2025 lúc 10:51 AM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `file_storage_db`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `files`
--

CREATE TABLE `files` (
  `FileID` bigint(20) NOT NULL,
  `file_name` varchar(255) NOT NULL,
  `stored_path` varchar(500) NOT NULL,
  `file_size` bigint(20) DEFAULT NULL,
  `file_type` varchar(255) DEFAULT NULL,
  `upload_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `owner_id` bigint(20) NOT NULL,
  `folder_id` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `file_versions`
--

CREATE TABLE `file_versions` (
  `VersionID` bigint(20) NOT NULL,
  `version_number` int(11) NOT NULL,
  `stored_path` varchar(500) NOT NULL,
  `notes` text DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `file_id` bigint(20) NOT NULL,
  `uploader_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `folders`
--

CREATE TABLE `folders` (
  `folderID` bigint(20) NOT NULL,
  `name_folder` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `owner_id` bigint(20) NOT NULL,
  `parent_folderID` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `groups`
--

CREATE TABLE `groups` (
  `GroupID` bigint(20) NOT NULL,
  `group_name` varchar(100) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `owner_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `group_members`
--

CREATE TABLE `group_members` (
  `id` bigint(20) NOT NULL,
  `role` varchar(50) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `group_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `public_links`
--

CREATE TABLE `public_links` (
  `LinkID` bigint(20) NOT NULL,
  `token` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `expires_at` datetime DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `file_id` bigint(20) DEFAULT NULL,
  `folder_id` bigint(20) DEFAULT NULL
) ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `share_file`
--

CREATE TABLE `share_file` (
  `ShareID` bigint(20) NOT NULL,
  `permission` varchar(50) NOT NULL,
  `shared_by` bigint(20) NOT NULL,
  `file_id` bigint(20) DEFAULT NULL,
  `folder_id` bigint(20) DEFAULT NULL,
  `shared_with_user_id` bigint(20) DEFAULT NULL,
  `share_with_group_id` bigint(20) DEFAULT NULL
) ;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `trash`
--

CREATE TABLE `trash` (
  `TrashID` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `file_id` bigint(20) DEFAULT NULL,
  `folder_id` bigint(20) DEFAULT NULL,
  `delet_at` timestamp NOT NULL DEFAULT current_timestamp(),
  `expire_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `users`
--

CREATE TABLE `users` (
  `UserID` bigint(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `files`
--
ALTER TABLE `files`
  ADD PRIMARY KEY (`FileID`),
  ADD KEY `owner_id` (`owner_id`),
  ADD KEY `folderId` (`folder_id`);

--
-- Chỉ mục cho bảng `file_versions`
--
ALTER TABLE `file_versions`
  ADD PRIMARY KEY (`VersionID`),
  ADD KEY `file_id` (`file_id`),
  ADD KEY `uploader_id` (`uploader_id`);

--
-- Chỉ mục cho bảng `folders`
--
ALTER TABLE `folders`
  ADD PRIMARY KEY (`folderID`),
  ADD KEY `owner_id` (`owner_id`),
  ADD KEY `Parent_folderID` (`parent_folderID`);

--
-- Chỉ mục cho bảng `groups`
--
ALTER TABLE `groups`
  ADD PRIMARY KEY (`GroupID`),
  ADD KEY `owner_id` (`owner_id`);

--
-- Chỉ mục cho bảng `group_members`
--
ALTER TABLE `group_members`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `group_id` (`group_id`);

--
-- Chỉ mục cho bảng `public_links`
--
ALTER TABLE `public_links`
  ADD PRIMARY KEY (`LinkID`),
  ADD UNIQUE KEY `token` (`token`),
  ADD KEY `file_id` (`file_id`),
  ADD KEY `folder_id` (`folder_id`);

--
-- Chỉ mục cho bảng `share_file`
--
ALTER TABLE `share_file`
  ADD PRIMARY KEY (`ShareID`),
  ADD KEY `SharedBy` (`shared_by`),
  ADD KEY `file_id` (`file_id`),
  ADD KEY `folder_id` (`folder_id`),
  ADD KEY `shared_with_user_id` (`shared_with_user_id`),
  ADD KEY `share_with_group_id` (`share_with_group_id`);

--
-- Chỉ mục cho bảng `trash`
--
ALTER TABLE `trash`
  ADD PRIMARY KEY (`TrashID`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `file_id` (`file_id`),
  ADD KEY `folder_id` (`folder_id`);

--
-- Chỉ mục cho bảng `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`UserID`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `files`
--
ALTER TABLE `files`
  MODIFY `FileID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `file_versions`
--
ALTER TABLE `file_versions`
  MODIFY `VersionID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `folders`
--
ALTER TABLE `folders`
  MODIFY `folderID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `groups`
--
ALTER TABLE `groups`
  MODIFY `GroupID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `group_members`
--
ALTER TABLE `group_members`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `public_links`
--
ALTER TABLE `public_links`
  MODIFY `LinkID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `share_file`
--
ALTER TABLE `share_file`
  MODIFY `ShareID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `trash`
--
ALTER TABLE `trash`
  MODIFY `TrashID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `users`
--
ALTER TABLE `users`
  MODIFY `UserID` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `files`
--
ALTER TABLE `files`
  ADD CONSTRAINT `files_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  ADD CONSTRAINT `files_ibfk_2` FOREIGN KEY (`folder_id`) REFERENCES `folders` (`folderID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `file_versions`
--
ALTER TABLE `file_versions`
  ADD CONSTRAINT `file_versions_ibfk_1` FOREIGN KEY (`file_id`) REFERENCES `files` (`FileID`) ON DELETE CASCADE,
  ADD CONSTRAINT `file_versions_ibfk_2` FOREIGN KEY (`uploader_id`) REFERENCES `users` (`UserID`);

--
-- Các ràng buộc cho bảng `folders`
--
ALTER TABLE `folders`
  ADD CONSTRAINT `folders_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  ADD CONSTRAINT `folders_ibfk_2` FOREIGN KEY (`Parent_folderID`) REFERENCES `folders` (`folderID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `groups`
--
ALTER TABLE `groups`
  ADD CONSTRAINT `groups_ibfk_1` FOREIGN KEY (`owner_id`) REFERENCES `users` (`UserID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `group_members`
--
ALTER TABLE `group_members`
  ADD CONSTRAINT `group_members_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  ADD CONSTRAINT `group_members_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `groups` (`GroupID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `public_links`
--
ALTER TABLE `public_links`
  ADD CONSTRAINT `public_links_ibfk_1` FOREIGN KEY (`file_id`) REFERENCES `files` (`FileID`) ON DELETE CASCADE,
  ADD CONSTRAINT `public_links_ibfk_2` FOREIGN KEY (`folder_id`) REFERENCES `folders` (`folderID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `share_file`
--
ALTER TABLE `share_file`
  ADD CONSTRAINT `share_file_ibfk_1` FOREIGN KEY (`shared_by`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  ADD CONSTRAINT `share_file_ibfk_2` FOREIGN KEY (`file_id`) REFERENCES `files` (`FileID`) ON DELETE CASCADE,
  ADD CONSTRAINT `share_file_ibfk_3` FOREIGN KEY (`folder_id`) REFERENCES `folders` (`folderID`) ON DELETE CASCADE,
  ADD CONSTRAINT `share_file_ibfk_4` FOREIGN KEY (`shared_with_user_id`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  ADD CONSTRAINT `share_file_ibfk_5` FOREIGN KEY (`share_with_group_id`) REFERENCES `groups` (`GroupID`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `trash`
--
ALTER TABLE `trash`
  ADD CONSTRAINT `trash_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`UserID`) ON DELETE CASCADE,
  ADD CONSTRAINT `trash_ibfk_2` FOREIGN KEY (`file_id`) REFERENCES `files` (`FileID`) ON DELETE CASCADE,
  ADD CONSTRAINT `trash_ibfk_3` FOREIGN KEY (`folder_id`) REFERENCES `folders` (`folderID`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
