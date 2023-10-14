package si.ita.services.users.userservice.messaging;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.54.0)",
    comments = "Source: messaging.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class MessagingGrpc {

  private MessagingGrpc() {}

  public static final String SERVICE_NAME = "Messaging";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<si.ita.services.users.userservice.messaging.CreateRequest,
      si.ita.services.users.userservice.messaging.CreateResponse> getSendMessageToQueueMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SendMessageToQueue",
      requestType = si.ita.services.users.userservice.messaging.CreateRequest.class,
      responseType = si.ita.services.users.userservice.messaging.CreateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<si.ita.services.users.userservice.messaging.CreateRequest,
      si.ita.services.users.userservice.messaging.CreateResponse> getSendMessageToQueueMethod() {
    io.grpc.MethodDescriptor<si.ita.services.users.userservice.messaging.CreateRequest, si.ita.services.users.userservice.messaging.CreateResponse> getSendMessageToQueueMethod;
    if ((getSendMessageToQueueMethod = MessagingGrpc.getSendMessageToQueueMethod) == null) {
      synchronized (MessagingGrpc.class) {
        if ((getSendMessageToQueueMethod = MessagingGrpc.getSendMessageToQueueMethod) == null) {
          MessagingGrpc.getSendMessageToQueueMethod = getSendMessageToQueueMethod =
              io.grpc.MethodDescriptor.<si.ita.services.users.userservice.messaging.CreateRequest, si.ita.services.users.userservice.messaging.CreateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SendMessageToQueue"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  si.ita.services.users.userservice.messaging.CreateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  si.ita.services.users.userservice.messaging.CreateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MessagingMethodDescriptorSupplier("SendMessageToQueue"))
              .build();
        }
      }
    }
    return getSendMessageToQueueMethod;
  }

  private static volatile io.grpc.MethodDescriptor<si.ita.services.users.userservice.messaging.CreateRequest,
      si.ita.services.users.userservice.messaging.CreateResponse> getReadMessagesFromQueueMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ReadMessagesFromQueue",
      requestType = si.ita.services.users.userservice.messaging.CreateRequest.class,
      responseType = si.ita.services.users.userservice.messaging.CreateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<si.ita.services.users.userservice.messaging.CreateRequest,
      si.ita.services.users.userservice.messaging.CreateResponse> getReadMessagesFromQueueMethod() {
    io.grpc.MethodDescriptor<si.ita.services.users.userservice.messaging.CreateRequest, si.ita.services.users.userservice.messaging.CreateResponse> getReadMessagesFromQueueMethod;
    if ((getReadMessagesFromQueueMethod = MessagingGrpc.getReadMessagesFromQueueMethod) == null) {
      synchronized (MessagingGrpc.class) {
        if ((getReadMessagesFromQueueMethod = MessagingGrpc.getReadMessagesFromQueueMethod) == null) {
          MessagingGrpc.getReadMessagesFromQueueMethod = getReadMessagesFromQueueMethod =
              io.grpc.MethodDescriptor.<si.ita.services.users.userservice.messaging.CreateRequest, si.ita.services.users.userservice.messaging.CreateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ReadMessagesFromQueue"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  si.ita.services.users.userservice.messaging.CreateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  si.ita.services.users.userservice.messaging.CreateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new MessagingMethodDescriptorSupplier("ReadMessagesFromQueue"))
              .build();
        }
      }
    }
    return getReadMessagesFromQueueMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MessagingStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MessagingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MessagingStub>() {
        @java.lang.Override
        public MessagingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MessagingStub(channel, callOptions);
        }
      };
    return MessagingStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MessagingBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MessagingBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MessagingBlockingStub>() {
        @java.lang.Override
        public MessagingBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MessagingBlockingStub(channel, callOptions);
        }
      };
    return MessagingBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static MessagingFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<MessagingFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<MessagingFutureStub>() {
        @java.lang.Override
        public MessagingFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new MessagingFutureStub(channel, callOptions);
        }
      };
    return MessagingFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void sendMessageToQueue(si.ita.services.users.userservice.messaging.CreateRequest request,
        io.grpc.stub.StreamObserver<si.ita.services.users.userservice.messaging.CreateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSendMessageToQueueMethod(), responseObserver);
    }

    /**
     */
    default void readMessagesFromQueue(si.ita.services.users.userservice.messaging.CreateRequest request,
        io.grpc.stub.StreamObserver<si.ita.services.users.userservice.messaging.CreateResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReadMessagesFromQueueMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service Messaging.
   */
  public static abstract class MessagingImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return MessagingGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service Messaging.
   */
  public static final class MessagingStub
      extends io.grpc.stub.AbstractAsyncStub<MessagingStub> {
    private MessagingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessagingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MessagingStub(channel, callOptions);
    }

    /**
     */
    public void sendMessageToQueue(si.ita.services.users.userservice.messaging.CreateRequest request,
        io.grpc.stub.StreamObserver<si.ita.services.users.userservice.messaging.CreateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getSendMessageToQueueMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void readMessagesFromQueue(si.ita.services.users.userservice.messaging.CreateRequest request,
        io.grpc.stub.StreamObserver<si.ita.services.users.userservice.messaging.CreateResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getReadMessagesFromQueueMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service Messaging.
   */
  public static final class MessagingBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<MessagingBlockingStub> {
    private MessagingBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessagingBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MessagingBlockingStub(channel, callOptions);
    }

    /**
     */
    public si.ita.services.users.userservice.messaging.CreateResponse sendMessageToQueue(si.ita.services.users.userservice.messaging.CreateRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getSendMessageToQueueMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<si.ita.services.users.userservice.messaging.CreateResponse> readMessagesFromQueue(
        si.ita.services.users.userservice.messaging.CreateRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getReadMessagesFromQueueMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service Messaging.
   */
  public static final class MessagingFutureStub
      extends io.grpc.stub.AbstractFutureStub<MessagingFutureStub> {
    private MessagingFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MessagingFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new MessagingFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<si.ita.services.users.userservice.messaging.CreateResponse> sendMessageToQueue(
        si.ita.services.users.userservice.messaging.CreateRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getSendMessageToQueueMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_SEND_MESSAGE_TO_QUEUE = 0;
  private static final int METHODID_READ_MESSAGES_FROM_QUEUE = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_SEND_MESSAGE_TO_QUEUE:
          serviceImpl.sendMessageToQueue((si.ita.services.users.userservice.messaging.CreateRequest) request,
              (io.grpc.stub.StreamObserver<si.ita.services.users.userservice.messaging.CreateResponse>) responseObserver);
          break;
        case METHODID_READ_MESSAGES_FROM_QUEUE:
          serviceImpl.readMessagesFromQueue((si.ita.services.users.userservice.messaging.CreateRequest) request,
              (io.grpc.stub.StreamObserver<si.ita.services.users.userservice.messaging.CreateResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getSendMessageToQueueMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              si.ita.services.users.userservice.messaging.CreateRequest,
              si.ita.services.users.userservice.messaging.CreateResponse>(
                service, METHODID_SEND_MESSAGE_TO_QUEUE)))
        .addMethod(
          getReadMessagesFromQueueMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              si.ita.services.users.userservice.messaging.CreateRequest,
              si.ita.services.users.userservice.messaging.CreateResponse>(
                service, METHODID_READ_MESSAGES_FROM_QUEUE)))
        .build();
  }

  private static abstract class MessagingBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    MessagingBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return si.ita.services.users.userservice.messaging.MessagingProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Messaging");
    }
  }

  private static final class MessagingFileDescriptorSupplier
      extends MessagingBaseDescriptorSupplier {
    MessagingFileDescriptorSupplier() {}
  }

  private static final class MessagingMethodDescriptorSupplier
      extends MessagingBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    MessagingMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (MessagingGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new MessagingFileDescriptorSupplier())
              .addMethod(getSendMessageToQueueMethod())
              .addMethod(getReadMessagesFromQueueMethod())
              .build();
        }
      }
    }
    return result;
  }
}
