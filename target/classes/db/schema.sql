/* 開発用にデータ削除を追加 : リリース時は消す
DROP TABLE task_t;
*/

/* タスクテーブル */
CREATE TABLE IF NOT EXISTS task_t (
  id  IDENTITY PRIMARY KEY,
  user_id VARCHAR(50),
  title VARCHAR(50),
  limitday DATE,
  complete BOOLEAN
);

/* ユーザマスタ */
CREATE TABLE IF NOT EXISTS user_m (
	id IDENTITY PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE,
    encrypted_password VARCHAR(100),
    user_name VARCHAR(50),
    role VARCHAR(50),
    enabled BOOLEAN
);

/* プロフィールマスタ */
CREATE TABLE IF NOT EXISTS profile_m (
	id IDENTITY PRIMARY KEY,
	user_id VARCHAR(50) UNIQUE,
	user_name VARCHAR(50),
	qualification VARCHAR(255),
	nickname VARCHAR(50),
	self_comment VARCHAR(100)
); 