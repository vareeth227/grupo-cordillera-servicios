# GitHub Secrets necesarios para CI/CD

Para usar el workflow de GitHub Actions, agrega los siguientes secrets:

## 1. Ir a GitHub
https://github.com/[tu-usuario]/grupo-cordillera-servicios/settings/secrets/actions

## 2. Agregar los siguientes secrets:

### EC2 Deployment
```
EC2_HOST              = tu-instancia-ec2.compute-1.amazonaws.com
EC2_USER              = ec2-user (o ubuntu)
EC2_PRIVATE_KEY       = Contenido de tu private key (.pem file)
EC2_PROJECT_PATH      = /home/ec2-user/grupo-cordillera-servicios
```

### Opcional: Slack Notifications
```
SLACK_WEBHOOK_URL     = https://hooks.slack.com/services/YOUR/WEBHOOK/URL
```

### Opcional: Docker Registry (si usas privado)
```
REGISTRY_USERNAME     = usuario
REGISTRY_PASSWORD     = contraseña o token
```

---

## Generar EC2_PRIVATE_KEY

Si tienes el archivo .pem:

```bash
# Copiar contenido
cat your-key.pem

# Y pegarlo en GitHub > Settings > Secrets > New Repository Secret
# Nombre: EC2_PRIVATE_KEY
# Valor: [contenido del archivo]
```

---

## Flujo del Workflow

```
1. Push a main branch
  ↓
2. Build Maven (compilación)
  ↓
3. Run Tests
  ↓
4. Build Docker Images
  ↓
5. Push to GitHub Container Registry
  ↓
6. SSH Deploy a EC2
  ↓
7. Health Check
  ↓
8. Notificar a Slack (opcional)
```

---

## Ver ejecución

1. Ir a: https://github.com/[usuario]/grupo-cordillera-servicios/actions
2. Ver workflow en tiempo real
3. Click en push para ver detalles

---

## Troubleshooting

### Deploy falla por SSH
- Verificar que EC2_PRIVATE_KEY está completo (incluir BEGIN/END PRIVATE KEY)
- Verificar que EC2_HOST es accesible desde GitHub
- Verificar que EC2_USER existe en la instancia

### Docker push falla
- Verificar que GitHub token tiene permisos en Settings > Developer settings

### Health check falla
- Ver logs en EC2: `docker logs -f api-gateway`
- Aumentar tiempo de espera si startup lento
