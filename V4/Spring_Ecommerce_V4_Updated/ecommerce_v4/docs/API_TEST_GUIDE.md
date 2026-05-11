# Guide de Test des Workflows API v4

Le serveur doit être lancé sur le port **8081**.

---

## 1. Configuration initiale

### Créer des catégories
```bash
curl -X POST http://localhost:8081/api/v1/categories \
-H "Content-Type: application/json" \
-d '{"name": "Électronique", "description": "Appareils high-tech"}'

curl -X POST http://localhost:8081/api/v1/categories \
-H "Content-Type: application/json" \
-d '{"name": "Promotions", "description": "Articles en solde"}'
```

### Créer un code promo
```bash
curl -X POST http://localhost:8081/api/v1/promotions \
-H "Content-Type: application/json" \
-d '{"code": "PROMO10", "discountPercentage": 10.0, "isActive": true}'
```

### Créer un utilisateur (numéro camerounais obligatoire)
```bash
# ✅ Numéro valide
curl -X POST http://localhost:8081/api/v1/users \
-H "Content-Type: application/json" \
-d '{
  "username": "jean_dupont",
  "email": "jean@example.cm",
  "password": "password123",
  "phoneNumber": "+237670000000",
  "role": "CUSTOMER"
}'

# ❌ Numéro invalide → 400
curl -X POST http://localhost:8081/api/v1/users \
-H "Content-Type: application/json" \
-d '{"username": "bad", "email": "bad@x.com", "password": "pass", "phoneNumber": "+33123456789"}'
```

---

## 2. Produits

### Créer un produit multi-catégories
```bash
curl -X POST http://localhost:8081/api/v1/products \
-H "Content-Type: application/json" \
-d '{
  "name": "iPhone 15",
  "description": "Dernier smartphone Apple",
  "price": 1200.0,
  "stockQuantity": 10,
  "categories": [{"id": 1, "name": "Électronique"}]
}'
```

### Ajouter une variante
```bash
curl -X POST http://localhost:8081/api/v1/products/1/variants \
-H "Content-Type: application/json" \
-d '{"type": "Couleur", "value": "Noir", "priceAdjustment": 0.0}'
```

### Ajouter un commentaire
```bash
curl -X POST http://localhost:8081/api/v1/products/1/comments \
-H "Content-Type: application/json" \
-d '{"userId": 1, "content": "Excellent produit !", "rating": 5}'
```

### Recherche avancée
```bash
curl "http://localhost:8081/api/v1/products?keyword=iPhone"
curl "http://localhost:8081/api/v1/products?category=Électronique&minPrice=1000&maxPrice=1500"
```

---

## 3. Commandes

### Créer une commande (body JSON complet)
```bash
curl -X POST http://localhost:8081/api/v1/orders \
-H "Content-Type: application/json" \
-d '{
  "userId": 1,
  "address": "Douala, Cameroun",
  "promoCode": "PROMO10",
  "items": [
    {"productId": 1, "quantity": 1}
  ]
}'
```

### Payer (génère PDF + envoi email automatique)
```bash
curl -X POST http://localhost:8081/api/v1/orders/1/pay
```

### Mettre à jour la livraison (body JSON)
```bash
curl -X PATCH http://localhost:8081/api/v1/orders/1/shipping \
-H "Content-Type: application/json" \
-d '{"status": "SHIPPED"}'

curl -X PATCH http://localhost:8081/api/v1/orders/1/shipping \
-H "Content-Type: application/json" \
-d '{"status": "DELIVERED"}'
```

---

## 4. Tickets de support

### Créer un ticket
```bash
curl -X POST http://localhost:8081/api/v1/tickets \
-H "Content-Type: application/json" \
-d '{"userId": 1, "subject": "Problème livraison", "message": "Ma commande est en retard."}'
```

### Fermer un ticket
```bash
curl -X PATCH http://localhost:8081/api/v1/tickets/1/close
```

---

## 5. Statistiques et ressources

```bash
curl http://localhost:8081/api/v1/stats
curl http://localhost:8081/api/v1/resources
```

---

## 6. Configuration mail (V4)

Les paramètres SMTP sont désormais **dans `application.properties`** et non dans la requête :

```properties
app.mail.from=noreply.ecommerce@gmail.com
app.mail.from-name=E-Commerce Shop
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=noreply.ecommerce@gmail.com
spring.mail.password=your_app_password_here
```

Pour tester localement avec MailHog ou Mailpit :
```properties
spring.mail.host=localhost
spring.mail.port=1025
spring.mail.properties.mail.smtp.auth=false
spring.mail.properties.mail.smtp.starttls.enable=false
app.mail.from=test@localhost
app.mail.from-name=E-Commerce Dev
```
