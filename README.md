<h1> Instruções para compilar o programa </h1>

1. Abra o terminal de comandos na pasta onde esse arquivo se encontra.
2. Digite o seguinte comando:<br> 

        javac com/br/Main.java @sources.txt -d bin

3. Note que <span style="color:yellow">bin</span> é a pasta de destino dos arquivos <span style="color:yellow">.class</span>.
4. Em seguida entre na pasta <span style="color:yellow">bin</span> que contém os arquivos <span style="color:yellow">.class</span>.
5. Abra o terminal novamente e digite o seguinte comando:<br>

         jar cfvm app.jar META-INF/MANIFEST.MF *

6. Note que <span style="color:yellow">app</span> é o nome de saída do arquivo jar. Você pode utilizar o nome que desejar.
5. Dê um duplo clique no arquivo <span style="color:yellow">app.jar</span>.

<h2> Em caso de <span style="color:#ee2200">Erros</span></h2>

- <h3>Verifique a <span style="color:yellow">versão do Java</span></h3>

    É indicado que você possua a versão <span style="color:yellow">18.0.2.1 2022-08-18</span> do <span style="color:yellow">Java</span> instalado em sua máquina.

    Você pode verificar a versão do Java utilizando o seguinte comando  no terminal:

        java -version

- <h3> Verifique o arquivo <span style="color:yellow">MANIFEST.MF</span></h3>

    Em alguns casos, o arquivo <span style="color:yellow">MANIFEST.MF</span> presente na pasta <span style="color:yellow">META-INF </span> pode ser substituído por um novo arquivo criado pela JVM do Java.
    Isso pode causar o erro <span style="color:yellow">"ClassNotFoundException"</span>.

    Para resolver esse problema, abra o arquivo <span style="color:yellow">".jar"</span> gerado na instruções anteriores utilizando um descompactor de arquivos, como por exemplo o <span style="color:yellow">"Winrar"</span>. Entre na pasta <span style="color:yellow">META-INF</span> e abra o arquivo <span style="color:yellow">MANIFEST.MF</span> com um editor de texto de sua preferência. Verifique se o arquivo contém a seguinte linha:

        Main-Class: com.br.Main

    Caso não exista, <span style="color:yellow">crie uma nova linha exatamente idêntica</span> a linha acima, salve o arquivo <span style="color:yellow">MANIFEST.MF</span>. Salve o arquivo <span style="color:yellow"> .jar</span>.
    Após inserir a nova linha, deixe uma linha em branco na última linha. Isso é necessário para a JVM compreender o arquivo MANIFEST


