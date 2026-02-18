Project covers basics of microservices using Kubernetes and Azure, along with CI/DC for git, in education purposes of subject Cloud Computing, FTN Novi Sad

# Cloud Orders - Projektni zadatak

**Predmet:** Računarstvo u oblaku

**Student:** Ognjen Zeković
**Broj indeksa:** E241/2025

## Arhitektura

- **Catalog Service** - Spring Boot, upravljanje proizvodima
- **Order Service** - Spring Boot, kreiranje narudžbina
- **Invoice Worker** - Spring Boot, asinhrono generisanje PDF faktura
- **Frontend** - React
- **Baza** - PostgreSQL (StatefulSet na AKS)

## Deploy

- AKS: catalog-service, order-service, frontend, PostgreSQL
- Azure Container Apps: invoice-worker
- Azure Storage: Queue (asinhrona komunikacija), Blob (PDF fakture)
- Azure Key Vault: centralizovano čuvanje tajni (CSI Driver)
- CI/CD: GitHub Actions (test → build → deploy → integracioni testovi + rollback)
