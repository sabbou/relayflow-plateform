insert into users (id, email, password_hash, role)
values (
  gen_random_uuid(),
  'admin@relayflow.com',
  '$2b$10$Z2d3DO7/l19.hKtGkjypG.9U3nKvrGemD9Ofn41uh7PzH78RV.PCi',
  'ADMIN'
)
on conflict (email) do nothing;
--gen_random_uuid() nécessite l’extension pgcrypto
--Si je ne l’ai pas,  je mets un UUID fixe