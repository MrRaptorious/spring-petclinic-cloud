DROP TABLE invoices IF EXISTS;

CREATE TABLE invoices (
  id          INTEGER IDENTITY PRIMARY KEY,
  amount      DECIMAL(10,2) NOT NULL,
  due_date    DATE,
  status      VARCHAR(32) DEFAULT 'OPEN',
  visit_id    INTEGER NOT NULL
);

CREATE INDEX invoices_visit_id ON invoices (visit_id);
