## Instruções para compilar o programa


1. Abra o terminal de comandos na pasta onde esse arquivo se encontra.
2. Digite o seguinte comando:

    javac com/br/Main.java @sources.txt -d bin

3. Note que bin é a pasta de destino dos arquivos .class.
4. Em seguida entre na pasta bin que contém os arquivois .class.

5. Abra o terminal novamente e digite o seguinte comando:

    jar cfvm app.jar META-INF/MANIFEST.MF *

6. Note que app é o nome de saída do arquivo jar. Você pode utilizar o nome que desejar.
7. Dê um duplo clique no arquivo app.jar.



## Em caso de Erros

1. Verifique a versão do Java:
    É indicado que você possua a versão 18.0.2.1 2022-08-18 do Java instalado em sua máquina.
    Você pode verificar a versão do Java utilizando o seguinte comando no terminal:

        java -version

2. Verifique o arquivo MANIFEST.MF:
    Em alguns casos, o arquivo MANIFEST.MF presente na pasta META-INF pode ser substituído por um novo arquivo criado pela JVM do Java. Isso pode causar o erro "ClassNotFoundException".
    Para resolver esse problema, abra o arquivo ".jar" gerado na instruções anteriores utilizando um descompactor de arquivos, como por exemplo o "Winrar". Entre na META-INF e abra o arquivo MANIFEST.MF com um editor de texto de sua preferência. Verifique se o arquivo contém a seguinte linha:
  
        Main-Class: com.br.Main

    Caso não exista, crie uma nova linha exatamente idêntica a linha acima, salve o arquivo MANIFEST.MF. Salve o arquivo .jar e execute novamente.