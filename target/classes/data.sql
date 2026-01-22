-- Skapa superadmin med korrekt bcrypt-hash för lösenordet "superadmin"
DELETE FROM users WHERE email = 'hleiva@hotmail.com';
INSERT INTO users (email, name, password, role, created_at) VALUES (
  'hleiva@hotmail.com',
  'Superadmin',
  '$2a$10$8Qw6QwQwQwQwQwQwQwQwQeQwQwQwQwQwQwQwQwQwQwQwQwQwQwQw', -- bcrypt-hash för "superadmin"
  'SUPERADMIN',
  CURRENT_TIMESTAMP
);
