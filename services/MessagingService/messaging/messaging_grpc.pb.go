// Code generated by protoc-gen-go-grpc. DO NOT EDIT.
// versions:
// - protoc-gen-go-grpc v1.2.0
// - protoc             v4.22.2
// source: messaging.proto

package messaging

import (
	context "context"
	grpc "google.golang.org/grpc"
	codes "google.golang.org/grpc/codes"
	status "google.golang.org/grpc/status"
)

// This is a compile-time assertion to ensure that this generated file
// is compatible with the grpc package it is being compiled against.
// Requires gRPC-Go v1.32.0 or later.
const _ = grpc.SupportPackageIsVersion7

// MessagingClient is the client API for Messaging service.
//
// For semantics around ctx use and closing/ending streaming RPCs, please refer to https://pkg.go.dev/google.golang.org/grpc/?tab=doc#ClientConn.NewStream.
type MessagingClient interface {
	SendMessageToQueue(ctx context.Context, in *CreateRequest, opts ...grpc.CallOption) (*CreateResponse, error)
	ReadMessagesFromQueue(ctx context.Context, in *CreateRequest, opts ...grpc.CallOption) (Messaging_ReadMessagesFromQueueClient, error)
}

type messagingClient struct {
	cc grpc.ClientConnInterface
}

func NewMessagingClient(cc grpc.ClientConnInterface) MessagingClient {
	return &messagingClient{cc}
}

func (c *messagingClient) SendMessageToQueue(ctx context.Context, in *CreateRequest, opts ...grpc.CallOption) (*CreateResponse, error) {
	out := new(CreateResponse)
	err := c.cc.Invoke(ctx, "/Messaging/SendMessageToQueue", in, out, opts...)
	if err != nil {
		return nil, err
	}
	return out, nil
}

func (c *messagingClient) ReadMessagesFromQueue(ctx context.Context, in *CreateRequest, opts ...grpc.CallOption) (Messaging_ReadMessagesFromQueueClient, error) {
	stream, err := c.cc.NewStream(ctx, &Messaging_ServiceDesc.Streams[0], "/Messaging/ReadMessagesFromQueue", opts...)
	if err != nil {
		return nil, err
	}
	x := &messagingReadMessagesFromQueueClient{stream}
	if err := x.ClientStream.SendMsg(in); err != nil {
		return nil, err
	}
	if err := x.ClientStream.CloseSend(); err != nil {
		return nil, err
	}
	return x, nil
}

type Messaging_ReadMessagesFromQueueClient interface {
	Recv() (*CreateResponse, error)
	grpc.ClientStream
}

type messagingReadMessagesFromQueueClient struct {
	grpc.ClientStream
}

func (x *messagingReadMessagesFromQueueClient) Recv() (*CreateResponse, error) {
	m := new(CreateResponse)
	if err := x.ClientStream.RecvMsg(m); err != nil {
		return nil, err
	}
	return m, nil
}

// MessagingServer is the server API for Messaging service.
// All implementations must embed UnimplementedMessagingServer
// for forward compatibility
type MessagingServer interface {
	SendMessageToQueue(context.Context, *CreateRequest) (*CreateResponse, error)
	ReadMessagesFromQueue(*CreateRequest, Messaging_ReadMessagesFromQueueServer) error
	mustEmbedUnimplementedMessagingServer()
}

// UnimplementedMessagingServer must be embedded to have forward compatible implementations.
type UnimplementedMessagingServer struct {
}

func (UnimplementedMessagingServer) SendMessageToQueue(context.Context, *CreateRequest) (*CreateResponse, error) {
	return nil, status.Errorf(codes.Unimplemented, "method SendMessageToQueue not implemented")
}
func (UnimplementedMessagingServer) ReadMessagesFromQueue(*CreateRequest, Messaging_ReadMessagesFromQueueServer) error {
	return status.Errorf(codes.Unimplemented, "method ReadMessagesFromQueue not implemented")
}
func (UnimplementedMessagingServer) mustEmbedUnimplementedMessagingServer() {}

// UnsafeMessagingServer may be embedded to opt out of forward compatibility for this service.
// Use of this interface is not recommended, as added methods to MessagingServer will
// result in compilation errors.
type UnsafeMessagingServer interface {
	mustEmbedUnimplementedMessagingServer()
}

func RegisterMessagingServer(s grpc.ServiceRegistrar, srv MessagingServer) {
	s.RegisterService(&Messaging_ServiceDesc, srv)
}

func _Messaging_SendMessageToQueue_Handler(srv interface{}, ctx context.Context, dec func(interface{}) error, interceptor grpc.UnaryServerInterceptor) (interface{}, error) {
	in := new(CreateRequest)
	if err := dec(in); err != nil {
		return nil, err
	}
	if interceptor == nil {
		return srv.(MessagingServer).SendMessageToQueue(ctx, in)
	}
	info := &grpc.UnaryServerInfo{
		Server:     srv,
		FullMethod: "/Messaging/SendMessageToQueue",
	}
	handler := func(ctx context.Context, req interface{}) (interface{}, error) {
		return srv.(MessagingServer).SendMessageToQueue(ctx, req.(*CreateRequest))
	}
	return interceptor(ctx, in, info, handler)
}

func _Messaging_ReadMessagesFromQueue_Handler(srv interface{}, stream grpc.ServerStream) error {
	m := new(CreateRequest)
	if err := stream.RecvMsg(m); err != nil {
		return err
	}
	return srv.(MessagingServer).ReadMessagesFromQueue(m, &messagingReadMessagesFromQueueServer{stream})
}

type Messaging_ReadMessagesFromQueueServer interface {
	Send(*CreateResponse) error
	grpc.ServerStream
}

type messagingReadMessagesFromQueueServer struct {
	grpc.ServerStream
}

func (x *messagingReadMessagesFromQueueServer) Send(m *CreateResponse) error {
	return x.ServerStream.SendMsg(m)
}

// Messaging_ServiceDesc is the grpc.ServiceDesc for Messaging service.
// It's only intended for direct use with grpc.RegisterService,
// and not to be introspected or modified (even as a copy)
var Messaging_ServiceDesc = grpc.ServiceDesc{
	ServiceName: "Messaging",
	HandlerType: (*MessagingServer)(nil),
	Methods: []grpc.MethodDesc{
		{
			MethodName: "SendMessageToQueue",
			Handler:    _Messaging_SendMessageToQueue_Handler,
		},
	},
	Streams: []grpc.StreamDesc{
		{
			StreamName:    "ReadMessagesFromQueue",
			Handler:       _Messaging_ReadMessagesFromQueue_Handler,
			ServerStreams: true,
		},
	},
	Metadata: "messaging.proto",
}
