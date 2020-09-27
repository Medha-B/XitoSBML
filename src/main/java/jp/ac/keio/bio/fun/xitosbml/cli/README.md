# XitoSBML_CLI

- Command Line Interface version for XitoSBML. 
- Allows the use of XitoSBML API from the command line to obtain spatial SBML models from the segmentations of microscopic cell images.
- Uses [Picocli](https://picocli.info/) as the command line parser which is a modern framework for building powerful, user-friendly, GraalVM-enabled command line applications.
- Currently only supports 'Cytoplasm' as the default cellular domain.


## How to Use

Instructions on: 

### Building
```sh
% git clone https://github.com/Medha-B/XitoSBML.git
```

### Compiling
```sh
% mvn compile
% mvn package   # create jar
```

### Running

The main class is CliRun.java which implements XitoSBML_CLI. It allows the user to access desired input images by specifying the directory of the dataset (or the path of a single input image) through command line argument ```sh -i ``` and obtain the resultant spatial SBML model at the location specified through the argument ```sh -o ```. 

- For input image file

```sh
% java -cp target/Xito_SBML-1.2.0-jar-with-dependencies.jar jp.ac.keio.bio.fun.xitosbml.cli.CliRun -i </path/to/image> -o </path/to/save/output/SBML/model>
```

- For input image folder

```sh
% java -cp target/Xito_SBML-1.2.0-jar-with-dependencies.jar jp.ac.keio.bio.fun.xitosbml.cli.CliRun -i </path/to/folder/containing/images> -o </path/to/save/output/SBML/model>
```
