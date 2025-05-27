DELETE FROM payment_schemes;

INSERT INTO payment_schemes (payment_scheme_id, name, number_of_payments, frequency, interest_rate) VALUES
(1, 'Scheme 1', 5, 14, 0.13),
(2, 'Scheme 2', 5, 14, 0.16);