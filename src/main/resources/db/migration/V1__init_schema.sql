CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Tabela de Usuários
CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR(100) NOT NULL,
                       email VARCHAR(150) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       global_role VARCHAR(20) NOT NULL DEFAULT 'USER',
                       reputation_score NUMERIC(3, 2) NOT NULL DEFAULT 5.00,
                       deleted BOOLEAN NOT NULL DEFAULT FALSE,
                       version BIGINT NOT NULL DEFAULT 0,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Comunidades
CREATE TABLE communities (
                             id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                             name VARCHAR(100) NOT NULL,
                             description VARCHAR(500),
                             invite_code VARCHAR(10) NOT NULL UNIQUE,
                             deleted BOOLEAN NOT NULL DEFAULT FALSE,
                             version BIGINT NOT NULL DEFAULT 0,
                             created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Associação de Membros
CREATE TABLE community_members (
                                   id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                   user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                   community_id UUID NOT NULL REFERENCES communities(id) ON DELETE CASCADE,
                                   role VARCHAR(30) NOT NULL,
                                   deleted BOOLEAN NOT NULL DEFAULT FALSE,
                                   version BIGINT NOT NULL DEFAULT 0,
                                   created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                   CONSTRAINT uk_user_community UNIQUE (user_id, community_id)
);

-- Tabela de Itens
CREATE TABLE items (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name VARCHAR(100) NOT NULL,
                       category VARCHAR(50) NOT NULL,
                       condition VARCHAR(20) NOT NULL,
                       status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
                       owner_id UUID NOT NULL REFERENCES users(id),
                       community_id UUID NOT NULL REFERENCES communities(id),
                       loan_rules VARCHAR(1000),
                       photo_urls TEXT[],
                       deleted BOOLEAN NOT NULL DEFAULT FALSE,
                       version BIGINT NOT NULL DEFAULT 0,
                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Reservas
CREATE TABLE reservations (
                              id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                              item_id UUID NOT NULL REFERENCES items(id),
                              borrower_id UUID NOT NULL REFERENCES users(id),
                              start_date DATE NOT NULL,
                              end_date DATE NOT NULL,
                              status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                              deleted BOOLEAN NOT NULL DEFAULT FALSE,
                              version BIGINT NOT NULL DEFAULT 0,
                              created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabela de Avaliações
CREATE TABLE reviews (
                         id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                         reservation_id UUID NOT NULL UNIQUE REFERENCES reservations(id),
                         evaluator_id UUID NOT NULL REFERENCES users(id),
                         evaluated_user_id UUID NOT NULL REFERENCES users(id),
                         rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
                         comment VARCHAR(1000),
                         deleted BOOLEAN NOT NULL DEFAULT FALSE,
                         version BIGINT NOT NULL DEFAULT 0,
                         created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices de Performance
CREATE INDEX idx_items_community_status ON items(community_id, status);
CREATE INDEX idx_reservations_item_dates ON reservations(item_id, start_date, end_date, status);
CREATE INDEX idx_community_members_user ON community_members(user_id);
CREATE INDEX idx_reviews_evaluated_user ON reviews(evaluated_user_id);