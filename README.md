# security-mig-demo

Progetto di esempio "legacy" che usa `WebSecurityConfigurerAdapter` (Spring Boot 2.6.6).
Serve per testare OpenRewrite e la migrazione verso `SecurityFilterChain`.

## Come usare

1. Controlla che Maven sia installato (Java 11+).
2. Esegui i test iniziali:
   ```
   mvn test
   ```
3. Esegui OpenRewrite con la ricetta di esempio (da terminale nella root del progetto):
   ```
   mvn -DactiveRecipes=org.openrewrite.java.spring.security5.UpgradeSpringSecurity_5_7 rewrite:run
   ```
   oppure usa il `rewrite.yml` incluso:
   ```
   mvn rewrite:run
   ```
   (Il plugin cercherà il `rewrite.yml` e applicherà la ricetta composita.)
4. Controlla i cambiamenti (diff) prodotti da OpenRewrite e rifinisci manualmente se necessario.

## Cosa contiene
- `WebConfig.java` — configurazione "vecchia" basata su `WebSecurityConfigurerAdapter`
- `rewrite.yml` — recipe composita che invoca la migrazione a Spring Security 5.7
