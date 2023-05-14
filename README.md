# Challenge Requirements

## Pix Simulator

In this challenge, we are going to simulate a bank transfer process via Pix, carried out through a mobile app. The process is very simple: the person using the application fills in a value and a Pix key and the application sends this information to the bank's server in the cloud. Our focus here will be to **deal with errors that may occur** throughout this operation.

Your main goal is to write two components in Java:

1. **Pix Processor:** is the component that contains the business logic of the operation. Given the inputs (value and Pix key), the Pix processor will be responsible for validating them and sending them to the cloud server, interpreting the response received.

1. **Pix Controller:** is the component that contains the presentation logic for this flow. It is responsible for invoking the Pix processor, passing the information filled in by the user on the application screen. It is also the component's responsibility to catch any errors that may occur during the process and inform the user about the result of the operation.

The Pix processor interacts with two other components that you won't need to program:

1. **Connection:** represents a communication channel between the mobile application and the cloud server. The Pix processor will use a connection to send the input data (Pix value and key).

1. **Server:** is the component that will be used by the Pix processor to open a new connection with the cloud server.


## Code Requirements

You have at your disposal the following components in Java:

- `Connection.java`
```java
import java.io.Closeable;
import java.io.IOException;

public interface Connection extends Closeable {

   String sendPix(int value, String key) throws IOException;
}
```

- `Server.java`
```java
import java.io.IOException;

public interface Server {

   Connection openConnection() throws IOException;
}
```

You must program the `PixProcessor` and `PixController` classes from the following templates:

- `PixProcessor.java`
```java
import java.io.IOException;

public class PixelProcessor {

   private final Server server;

   public PixProcessor(Server server) {
     this.server = server;
   }

   public void executePix(int value, String key) throws PixError, IOException {
     // TODO: Implement.
   }
}
```

- `PixController.java`
```java
import java.io.IOException;

public class PixController {

   private final PixelProcessor pixProcessor;

   public PixController(PixProcessor pixProcessor) {
     this.pixProcessor = pixProcessor;
   }

   public String whenConfirmingPix(int value, String key) {
     return null; // TODO: Implement.
   }
}
```

### Error Handling

During the operation of the Pix, it is possible that a series of errors occur:

- Locally validated application errors
     - Amount to be transferred less than or equal to zero
     - Blank Pix key

- Application errors validated by the server
     - Insufficient funds
     - Pix key not found
     - Internal error (e.g. server crash)

- Communication errors (e.g. connection timeout)

Each of these errors translates to a Java exception.

Communication errors are equivalent to exceptions derived from `java.io.IOException`. They can come either from the Java standard library, like `java.net.SocketException` and the like, or from libraries used to implement the application's communication with the server — which is outside the scope of this challenge.

As for application errors, there are no exceptions that represent them in the standard library. So let's create _custom exceptions_. You must create the following classes:

- `NoPositiveValueError`
- `BlankKeyError`
- `InsufficientBalanceError`
- `KeyNotFoundError`
- `InternalError`

As part of the project, you must use _exactly_ the names above to ensure delivery is in line with requirements. In addition, each of the above classes must extend the `PixError` class, whose code follows below:

- `PixError.java`
```java
public abstract class PixError extends Exception {

   public PixError(String message) {
     super(message);
   }
}
```

### `PixProcessor.executePix`

When implementing the `PixProcessor.executePix` method, you must follow these steps:

- Ensure that `value` (in cents) is a positive integer.
     - Otherwise, throw an `ErrorNoPositiveValue` exception.
- Ensure that the String `key` is not blank.
     - A String will be considered blank if it is empty or composed only of white spaces.
     - If the key is blank, throw `BlankKeyError` exception.
- Open a connection to the server.
- Use the connection to send the data.
- Interpret the server response.
- Close connection to the server.

When opening the connection and sending the data, it is possible that an exception derived from `java.io.IOException` is thrown. You shouldn't catch these exceptions here, let them propagate.

After sending the data, the server will send back a code that indicates the result of the operation, in this case the String returned by the `Connection.sendPix` method. You must proceed according to the code value:

- `success`: no action required.
- `insufficient_balance`: throw exception `InsufficientBalanceError`.
- `key_pix_not_found`: throw `KeyNotFoundError` exception.
- For any other value, let's assume that an internal error has occurred: throw an `InternalError` exception.

These codes are defined in the `ReturnCodes` class, which will be available to you:

- `ReturnCodes.java`
```java
public class ReturnCodes {

   public static final String SUCCESS = "success";
   public static final String SALDO_INSUFFICIENTE = "insufficient_balance";
   public static final String CHAVE_PIX_NAO_ENCONTRADA = "key_pix_not_found";
}
```

To close the connection with the server, you must call the `Connection.close` method.

⚠️ **Important!!** The connection to the server must always be closed, _even in case of an error_. Take care that the `Connection.close` method is called before returning from the `PixProcessor.executePix` method.

**Tip:** `Connection.close` comes from the `AutoCloseable` interface, provided by the Java standard library. By the way, this interface is treated in a special way by the compiler. There is a way to use features derived from `AutoCloseable`, ensuring that the `close` method is called at the end of the operation (even in case of error) without having to do it manually. 😉


### `PixController.whenConfirmingPix`

The `PixController.whenConfirmingPix` method represents the event of the user clicking a button on the screen of their mobile device to confirm the data they just filled in and proceed with the transfer. In implementing this method, you must follow these steps:

- Run the Pix using the `pixProcessor` object.
- Catch any exception that is thrown during the operation.
- Return a message to inform the user about the result of the operation.

⚠️ **Important!!** You must not "leak" any exceptions from here. An unhandled exception at this layer results in an application crash.

You must return the correct message according to the result:
- Success: `Pix created successfully.`
- Value less than or equal to zero: `The Pix value cannot be less than or equal to zero.`
- Key blank: `Key Pix cannot be blank.`
- Insufficient balance: `Your balance is insufficient.`
- Pix key not found: `Pix key not found.`
- Internal error: `Internal error.`
- Any exception derived from `IOException`: `Connection error.`

Again, we need you to use _exactly_ the messages above, fulfilling what was requested for the product. To avoid possible typing errors, the above messages will be available in the `Messages` class:

- `Messages.java`
```java
public class Messages {

   public static final String SUCCESS =
       "Pix created successfully.";

   public static final String NON_POSITIVE_VALUE =
       "Pix value cannot be less than or equal to zero.";

   public static final String BLANK_KEY =
       "Pix key cannot be blank.";

   public static final String INSUFFICIENT_BALANCE =
       "Your balance is insufficient.";

   public static final String KEY_NOT_FOUND =
       "Pix key not found.";

   public static final String INTERNAL_ERROR =
       "Internal error.";

   public static final String CONNECTION_ERROR =
       "Connection error.";
}
```

It is possible to validate the input data (Pix value and key) here, but you should not do that, as the validation will already have been done in `PixProcessor.executePix`.


## Code Validation

The classes you will program depend on interfaces that are not implemented anywhere. This makes it harder to run your code and make sure it's working as expected. To advance this part, we will provide you with the `FakeServer` and `Main` classes:

- `FakeServer.java`
```java
public class FakeServer implements Server {

   @Override
   public Connection openConnection() {
     return new Connection() {

       @Override
       public void close() {
       }

       @Override
       public String sendPix(int value, String key) {
         return ReturnCodes.SUCCESS;
       }
     };
   }
}
```

- `Main.java`
```java
public class Main {

   public static void main(String[] args) {
     Server server = new FakeServer();
     PixProcessor pixProcessor = newPixProcessor(server);
     PixController pixController = new PixController(pixProcessor);
     String message = PixController.whenConfirmingPix(2000, "abc123");
     System.out.println(message);
   }
}
```

To find out how your code behaves in different situations, try changing the returned values or throwing exceptions in the code of the `FakeServer` class.

Good luck! Keep an eye on every detail of the challenge, agreed? 👀

---