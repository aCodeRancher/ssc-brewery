INSERT INTO user (id, username, password, account_Non_Expired, account_Non_Locked, credentials_Non_Expired, enabled) VALUES(0,'spring', '{bcrypt}$2a$10$7tYAvVL2/KwcQTcQywHIleKueg4ZK7y7d44hKyngjTwHCDlesxdla', true, true, true, true);
INSERT INTO user (id, username, password, account_Non_Expired, account_Non_Locked, credentials_Non_Expired, enabled) VALUES(1, 'user', '{sha256}1296cefceb47413d3fb91ac7586a4625c33937b4d3109f5a4dd96c79c46193a029db713b96006ded', true, true, true, true );
INSERT INTO user (id, username, password, account_Non_Expired, account_Non_Locked, credentials_Non_Expired, enabled) VALUES(2, 'scott', '{bcrypt10}$2a$10$jv7rEbL65k4Q3d/mqG5MLuLDLTlg5oKoq2QOOojfB3e2awo.nlmgu', true,true, true, true);

INSERT INTO authority (id, role) VALUES (0,'ADMIN');
INSERT INTO authority (id, role) VALUES (1, 'USER');
INSERT INTO authority (id, role) VALUES (2, 'CUSTOMER');

INSERT INTO user_authority (USER_ID, AUTHORITY_ID) VALUES (0,0);
INSERT INTO user_authority (USER_ID, AUTHORITY_ID) VALUES (1,1);
INSERT INTO user_authority (USER_ID, AUTHORITY_ID) VALUES (2,2);
