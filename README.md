# Quartz Demo

Projeto de demonstração com Quartz.

### Parâmetros da aplicação

Para facilitar os testes e debug da aplicação, configurar para "true" o parâmetro "quartz.factory.simpleTrigger.enabled" e "false" o "quartz.factory.cronTrigger.enabled" no arquivo application.yml.
```yaml
quartz:
  factory:
    simpleTrigger:
      enabled: true
      startDelay: 5
      repeatInterval: 5
    cronTrigger:
      enabled: false
      startDelay: 10
      cron: 0 0 6-23 * * ?
```

### Cron

0 0 6-23 * * ? | Disparar todos os dias a cada hora das 06hs até 23hs.

Para mais exemplos de cron, consultar [aqui](http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/crontrigger.html "aqui").

### Documentação do Quartz

http://www.quartz-scheduler.org/documentation/
