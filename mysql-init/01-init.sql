-- Crea las 9 bases de datos de AnimaLink al iniciar el contenedor MySQL.
-- Docker ejecuta automáticamente los .sql de /docker-entrypoint-initdb.d la primera vez.
CREATE DATABASE IF NOT EXISTS animalink_auth         CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_usuario      CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_cliente      CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_mascota      CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_cita         CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_historial    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_control_alta CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_inventario   CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS animalink_factura      CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
