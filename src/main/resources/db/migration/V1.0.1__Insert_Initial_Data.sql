
INSERT INTO clients (name, sector, created_at) VALUES ('Tech Corp', 'IT', '2023-01-10 10:00:00');
INSERT INTO clients (name, sector, created_at) VALUES ('Health Plus', 'Healthcare', '2023-02-15 14:30:00');
INSERT INTO clients (name, sector, created_at, deleted_at) VALUES ('Old Store', 'Retail', '2022-05-20 09:15:00', '2023-11-01 10:00:00');
INSERT INTO clients (name, sector, created_at) VALUES ('Finance Pro', 'Finance', '2024-01-05 11:45:00');

INSERT INTO interactions (client_id, commercial, type, summary, occurred_at, duration, created_at) VALUES (1, 'Alice', 'CALL', 'Initial contact API discussions', '2023-01-12 10:00:00', 30, '2023-01-12 10:30:00');
INSERT INTO interactions (client_id, commercial, type, summary, occurred_at, duration, created_at) VALUES (1, 'Alice', 'MEETING', 'Architecture review', '2023-01-20 14:00:00', 60, '2023-01-20 15:00:00');
INSERT INTO interactions (client_id, commercial, type, summary, occurred_at, duration, created_at) VALUES (1, 'Bob', 'EMAIL', 'Follow up on the meeting', '2023-01-22 09:00:00', 5, '2023-01-22 09:05:00');
INSERT INTO interactions (client_id, commercial, type, summary, occurred_at, duration, created_at) VALUES (2, 'Charlie', 'MEETING', 'Demo presentation', '2023-02-20 10:00:00', 45, '2023-02-20 11:00:00');
INSERT INTO interactions (client_id, commercial, type, summary, occurred_at, duration, created_at) VALUES (3, 'Bob', 'CALL', 'Complaint about pricing', '2023-08-10 16:20:00', 15, '2023-08-10 16:35:00');
INSERT INTO interactions (client_id, commercial, type, summary, occurred_at, duration, created_at) VALUES (4, 'Alice', 'OTHER', 'Sent proposal for 2024', '2024-01-08 11:00:00', 10, '2024-01-08 11:10:00');
