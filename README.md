# Sustav za upravljanje osobnim financijama

REST API za upravljanje osobnim financijama izraden u Spring Boot-u. Omogucuje pracenje prihoda i rashoda, kategorizaciju transakcija, generiranje financijskih izvjestaja i vizualizaciju podataka.

## Tehnologije

- Java 17
- Spring Boot 3.3.6
- Spring Data JPA
- Spring Security + JWT autentifikacija
- PostgreSQL (Docker) / H2 (lokalni razvoj)
- Swagger/OpenAPI dokumentacija
- Docker & Docker Compose
- Maven
- Lombok
- JUnit 5 + Mockito

## Pokretanje

### Lokalno (H2 baza)

```bash
./mvnw spring-boot:run
```

Aplikacija koristi H2 in-memory bazu podataka. H2 konzola dostupna na: `http://localhost:8080/h2-console`

### Docker (PostgreSQL baza)

```bash
docker-compose up --build
```

Pokrece aplikaciju i PostgreSQL bazu u Docker kontejnerima.

## API dokumentacija

Swagger UI: `http://localhost:8080/swagger-ui.html`

## API endpointi

### Autentifikacija

| Metoda | Endpoint             | Opis                  |
|--------|----------------------|-----------------------|
| POST   | /api/auth/register   | Registracija korisnika|
| POST   | /api/auth/login      | Prijava korisnika     |

### Kategorije

| Metoda | Endpoint             | Opis                          |
|--------|----------------------|-------------------------------|
| POST   | /api/categories      | Kreiranje kategorije          |
| GET    | /api/categories      | Dohvat svih kategorija        |
| GET    | /api/categories/{id} | Dohvat kategorije po ID-u     |
| PUT    | /api/categories/{id} | Azuriranje kategorije         |
| DELETE | /api/categories/{id} | Brisanje kategorije           |

### Transakcije

| Metoda | Endpoint                  | Opis                              |
|--------|---------------------------|-----------------------------------|
| POST   | /api/transactions         | Kreiranje transakcije             |
| GET    | /api/transactions         | Dohvat svih transakcija           |
| GET    | /api/transactions/search  | Pretrazivanje i filtriranje       |
| GET    | /api/transactions/{id}    | Dohvat transakcije po ID-u        |
| PUT    | /api/transactions/{id}    | Azuriranje transakcije            |
| DELETE | /api/transactions/{id}    | Brisanje transakcije              |

### Izvjestaji

| Metoda | Endpoint              | Opis                    |
|--------|-----------------------|-------------------------|
| GET    | /api/reports/monthly  | Mjesecni izvjestaj      |
| GET    | /api/reports/yearly   | Godisnji izvjestaj      |

### Grafovi

| Metoda | Endpoint                          | Opis                              |
|--------|-----------------------------------|-----------------------------------|
| GET    | /api/charts/spending-by-category  | Potrosnja po kategoriji (pie)     |
| GET    | /api/charts/monthly-trend         | Mjesecni trend prihoda/rashoda    |

## Primjeri koristenja (curl)

### Registracija

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"korisnik","email":"korisnik@email.com","password":"lozinka123"}'
```

### Prijava

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"korisnik","password":"lozinka123"}'
```

### Kreiranje kategorije

```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"name":"Hrana","type":"EXPENSE"}'
```

### Kreiranje transakcije

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <TOKEN>" \
  -d '{"amount":150.00,"description":"Rucak","date":"2026-06-15","type":"EXPENSE","categoryId":1}'
```

### Mjesecni izvjestaj

```bash
curl http://localhost:8080/api/reports/monthly?year=2026&month=6 \
  -H "Authorization: Bearer <TOKEN>"
```

## Pokretanje testova

```bash
./mvnw test
```

## Zadani korisnik

Pri prvom pokretanju automatski se kreira korisnik:
- **Username:** admin
- **Password:** password123

S pripadajucim kategorijama: Food, Transport, Utilities, Entertainment, Salary, Other.

## Autor

Kristian Pejic
