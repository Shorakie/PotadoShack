# Potado Shack
Online food ordering service.
This is the Java CS course final term project.
The application is written in `Java` and is based on `JavaFX`.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

- JavaFX 10.0.0
- Java 8

### Installing
Simply clone the project into a directory

#### Libraries
##### JFoenix
The project uses `JFoenix` library as well.  
After downloading the library from [JFoenix](http://www.jfoenix.com) official site, add the library to the project.

## Running the Application

To run the Client
```
Java -jar PotadoShack.jar -h <HOST> -p <PORT>
```

To run the Server
```
Java -jar PotadoShack.jar -s -key <SECRET_KEY> -p <PORT>
```

## Starting arguments
- `--server` `-s` Start the server instead of client

- `--port <PORT>` `-p <PORT>` Set the port to connect / listen to. `defualt:25552`

- `--host <HOST>` `-h <HOST>` Set the host address to connect to. `default:'localhost'`

- `--key <KEY>` `--secret-key <KEY>` Set the secret key used for hashing

- `--accept-amount <COUNT>` `--thread-pool-size <COUNT>` Sets the amount of clients server can handel at the same time. `default:20`

## Built With

- [JavaFx](https://openjfx.io) - The ui framework used
- ❤ Love ❤ - Aminer

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## Authors

- **Amin Jafari** - *Initial work* - [AminerZ](https://gitlab.com/AminerZ)

## License

This project is licensed under the `TBD` License - see the [LICENSE.md](LICENSE.md) file for details.
