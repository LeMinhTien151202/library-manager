--
-- PostgreSQL database dump
--

-- Dumped from database version 16.8
-- Dumped by pg_dump version 16.8

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: books; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.books (book_id, title, author, publication_year, genre, created_at, thumbnail) VALUES (1, 'Lão Hạc', 'Nam Cao', 1943, 'Văn học hiện thực', '2025-04-21 22:31:54.582065', NULL);
INSERT INTO public.books (book_id, title, author, publication_year, genre, created_at, thumbnail) VALUES (2, 'Số Đỏ', 'Vũ Trọng Phụng', 1936, 'Văn học châm biếm', '2025-04-21 22:31:54.582065', NULL);
INSERT INTO public.books (book_id, title, author, publication_year, genre, created_at, thumbnail) VALUES (9, 'Hy theo duoi uoc mo', 'Hoang Cong Minh', 2024, 'Gia Thuyet', NULL, '/uploads/dc4e7a1f-2c07-4101-8648-df35fde6c68c_dai-hoc-mo-dia-chat-2.png');
INSERT INTO public.books (book_id, title, author, publication_year, genre, created_at, thumbnail) VALUES (10, 'Hy theo duoi uoc mo', 'Hoang Cong Minh', 2024, 'Gia Thuyet', '2025-04-22 14:23:23.212431', '/uploads/0a811682-bf3d-4576-a1b7-388d2ac4c546_003.jpg');
INSERT INTO public.books (book_id, title, author, publication_year, genre, created_at, thumbnail) VALUES (7, 'clean code', 'Robert c.matin', 2023, 'CNTT', NULL, '/uploads/3f32f8c6-80fe-4d22-8329-55e40ee097c1_001.jpg');
INSERT INTO public.books (book_id, title, author, publication_year, genre, created_at, thumbnail) VALUES (11, 'hihi', 'fdf', 2002, 'Hành động, Phiêu lưu', '2025-04-25 13:14:06.152349', '/uploads/3c607e08-2630-48e7-af4c-93543561cedc_7c605921-1ff1-405d-9f0c-536f57a5011d.jpg');
INSERT INTO public.books (book_id, title, author, publication_year, genre, created_at, thumbnail) VALUES (4, 'Canh Tranh', 'Thay Giang', 2002, 'Canh tranh', NULL, '/uploads/d69bc12f-8430-4e8e-90c4-f84dbaefa712_0bf78776-b21b-4cc3-8294-114b073b7441_thu3.jpg');


--
-- Data for Name: borrowers; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.borrowers (borrower_id, name, email, phone, created_at, id) VALUES (1, 'Nguyễn Văn A', 'nguyenvana@example.com', '0901234567', '2025-04-21 22:33:27.574323', 1);
INSERT INTO public.borrowers (borrower_id, name, email, phone, created_at, id) VALUES (2, 'Trần Thị B', 'tranthib@example.com', '0912345678', '2025-04-21 22:33:27.574323', 2);
INSERT INTO public.borrowers (borrower_id, name, email, phone, created_at, id) VALUES (3, 'Lê Văn C', 'levanc@example.com', '0923456789', '2025-04-21 22:33:27.574323', 3);
INSERT INTO public.borrowers (borrower_id, name, email, phone, created_at, id) VALUES (5, 'TienLe', 'hoang23@gmail.com', '0964896239', '2025-04-25 15:17:09.094504', 5);


--
-- Data for Name: borrows; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: borrowstatus; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.borrowstatus (borrow_id, book_id, borrower_id, borrow_date, return_date, status, created_at) VALUES (1, 1, 1, '2025-04-01', NULL, 'borrowed', '2025-04-21 22:37:21.628702');
INSERT INTO public.borrowstatus (borrow_id, book_id, borrower_id, borrow_date, return_date, status, created_at) VALUES (2, 2, 2, '2025-03-15', '2025-04-10', 'returned', '2025-04-21 22:37:21.628702');
INSERT INTO public.borrowstatus (borrow_id, book_id, borrower_id, borrow_date, return_date, status, created_at) VALUES (4, 1, 2, '2025-04-04', '2025-04-22', 'borrowed', '2025-04-25 14:32:37.958566');
INSERT INTO public.borrowstatus (borrow_id, book_id, borrower_id, borrow_date, return_date, status, created_at) VALUES (5, 1, 1, '2025-04-04', '2025-04-23', 'returned', '2025-04-25 14:32:54.984068');


--
-- Name: books_book_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.books_book_id_seq', 11, true);


--
-- Name: borrowers_borrower_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.borrowers_borrower_id_seq', 5, true);


--
-- Name: borrowers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.borrowers_id_seq', 5, true);


--
-- Name: borrows_borrow_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.borrows_borrow_id_seq', 1, false);


--
-- Name: borrows_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.borrows_id_seq', 1, false);


--
-- Name: borrowstatus_borrow_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.borrowstatus_borrow_id_seq', 5, true);


--
-- PostgreSQL database dump complete
--

