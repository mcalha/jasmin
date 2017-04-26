# Jasmin Assembly Emulator
Um fork do projeto Jasmin da Universidade Técnica de Munique. O desenvolvimento desta aplicação foi continuado por parte dos professores e alunos da FCUL.

Ao longo do README vai ser usado o acrónimo TUM para designar a Universidade Técnica de Munique.

**Não se esqueçam de alterar o JDK usado pelo NetBeans para remover quaisquer erros relacionados com --source 1.6**

Podem ler mais sobre isso [aqui](http://stackoverflow.com/questions/11110808/use-source-7-or-higher-to-enable-strings-in-switch-errornetbeans-7-1-2).

Basta carregarem em cima da tab Projects e com o botão direito em cima do "Jasmin". Depois de irem as properties
basta mudarem o "source/binary format" para o mais recente (o jasmin usa o mais velho por predefinição).
Seguem-se uns screenshots para ajudar.

[Step 1](https://i.imgsafe.org/e7c0f1decc.png)

[Step 2](https://i.imgsafe.org/e7c0f924e3.png)

[Step 3](https://i.imgsafe.org/e7c0fe94cc.png)

##
Vejam os commits da outra faculdade a partir de Jan 23 (até essa data foi tudo incluido)
https://github.com/TUM-LRR/Jasmin/commits/master

##
O projeto é constituido por dois branches.
* master - Desenvolvimento contínuo.
* stable - Um backup da última versão estável.

### Changelog
Master
```
Alpha 1 - Base para o desenvolvimento do Jasmin64 (2.0). Após a inclusão das otimizações que não afetavam a base do programa,
foram introduzidas alterações mais avançadas. Nesta segunda fase de updates, o código não era simples o suficiente para poder ser
considerado seguro. De acordo com os commits da TUM estão aqui incluidos "fixes" para alguns bugs bem como outras melhorias.
O código da FCUL, quando já tinha sido alterado desde o seu commit inicial, era considerado e comparado com o novo código da TUM.
Caso fossem compatíveis (algo que ocorreu sempre) eram íncluidas as duas alterações. Na sua maior parte tratou-se de otimizações
e não de "bug fixes" em si.

- Patch 1 - 

/commands/Lods.java - [correct LODSD bug]
/core/Parameters.java - TUM fizeram um major rework neste ficheiro acrescentando um enumerado de coisas novas e possivelmente 
resolvendo alguns bugs. Adicionei o nosso código alterado na esperança de manter o melhor dos dois mundos visto não me 
parecer haver nenhuma incompatibilidade. Aquela verificação que o professor removeu para resolver o problema da stack 
foi reposicionada pelo TUM. Decidi deixa la e testar como funciona agora. Preservaram-se os seguintes bug fixes já existentes
no nosso código: Run vs Step e Linux read call

Caso o bug Parameters.pop(Address a) volte a aparecer a única coisa que temos que fazer é apagar a verificação outra vez.

/core/Parser.java -[commands are now allowed as labels. parsing bug fix]

- Patch 2 - 

/core/Registers.java - o ficheiro do TUM estava vários commits à frente com bastantes alterações. Atualizaram
a função principal permitindo maior legibilidade e maior rapidez. Pelos vistos eles deram de caras com um
problema e resolveram-no.
/gui/FpuStackTableModel.java - [optimizations]
src/jasmin/gui/HelpBrowser.java - [optimizations]
/gui/JasDocument.java - [optimizations]
/gui/MainFrame.java - [optimizations]
/gui/MemoryTableModel.java - [optimizations]
/gui/RegisterPanel.java - [Quando um registo é alterado agora mostra que parte do registo foi] + questionable boxing fix
/gui/SyntaxHighlighter.java - [commands are now allowed as labels + otimizaçoes]
/gui/VGA.java - unboxed primitive value + [optimizations]

Todas as alterações entre parentes retos provêm da TUM e o restante foi preservado do nosso fork.
```


