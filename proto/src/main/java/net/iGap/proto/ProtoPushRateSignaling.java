// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: PushRateSignaling.proto

package net.iGap.proto;

public final class ProtoPushRateSignaling {
  private ProtoPushRateSignaling() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }

  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
    registerAllExtensions(
        (com.google.protobuf.ExtensionRegistryLite) registry);
  }
  public interface PushRateSignalingResponseOrBuilder extends
      // @@protoc_insertion_point(interface_extends:proto.PushRateSignalingResponse)
      com.google.protobuf.MessageOrBuilder {

    /**
     * <code>optional .proto.Response response = 1;</code>
     */
    boolean hasResponse();
    /**
     * <code>optional .proto.Response response = 1;</code>
     */
    net.iGap.proto.ProtoResponse.Response getResponse();
    /**
     * <code>optional .proto.Response response = 1;</code>
     */
    net.iGap.proto.ProtoResponse.ResponseOrBuilder getResponseOrBuilder();

    /**
     * <code>optional uint64 id = 2;</code>
     */
    long getId();
  }
  /**
   * Protobuf type {@code proto.PushRateSignalingResponse}
   */
  public  static final class PushRateSignalingResponse extends
      com.google.protobuf.GeneratedMessageV3 implements
      // @@protoc_insertion_point(message_implements:proto.PushRateSignalingResponse)
      PushRateSignalingResponseOrBuilder {
    // Use PushRateSignalingResponse.newBuilder() to construct.
    private PushRateSignalingResponse(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
      super(builder);
    }
    private PushRateSignalingResponse() {
      id_ = 0L;
    }

    @java.lang.Override
    public final com.google.protobuf.UnknownFieldSet
    getUnknownFields() {
      return com.google.protobuf.UnknownFieldSet.getDefaultInstance();
    }
    private PushRateSignalingResponse(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      this();
      int mutable_bitField0_ = 0;
      try {
        boolean done = false;
        while (!done) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              done = true;
              break;
            default: {
              if (!input.skipField(tag)) {
                done = true;
              }
              break;
            }
            case 10: {
              net.iGap.proto.ProtoResponse.Response.Builder subBuilder = null;
              if (response_ != null) {
                subBuilder = response_.toBuilder();
              }
              response_ = input.readMessage(net.iGap.proto.ProtoResponse.Response.parser(), extensionRegistry);
              if (subBuilder != null) {
                subBuilder.mergeFrom(response_);
                response_ = subBuilder.buildPartial();
              }

              break;
            }
            case 16: {

              id_ = input.readUInt64();
              break;
            }
          }
        }
      } catch (com.google.protobuf.InvalidProtocolBufferException e) {
        throw e.setUnfinishedMessage(this);
      } catch (java.io.IOException e) {
        throw new com.google.protobuf.InvalidProtocolBufferException(
            e).setUnfinishedMessage(this);
      } finally {
        makeExtensionsImmutable();
      }
    }
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return net.iGap.proto.ProtoPushRateSignaling.internal_static_proto_PushRateSignalingResponse_descriptor;
    }

    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return net.iGap.proto.ProtoPushRateSignaling.internal_static_proto_PushRateSignalingResponse_fieldAccessorTable
          .ensureFieldAccessorsInitialized(
              net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse.class, net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse.Builder.class);
    }

    public static final int RESPONSE_FIELD_NUMBER = 1;
    private net.iGap.proto.ProtoResponse.Response response_;
    /**
     * <code>optional .proto.Response response = 1;</code>
     */
    public boolean hasResponse() {
      return response_ != null;
    }
    /**
     * <code>optional .proto.Response response = 1;</code>
     */
    public net.iGap.proto.ProtoResponse.Response getResponse() {
      return response_ == null ? net.iGap.proto.ProtoResponse.Response.getDefaultInstance() : response_;
    }
    /**
     * <code>optional .proto.Response response = 1;</code>
     */
    public net.iGap.proto.ProtoResponse.ResponseOrBuilder getResponseOrBuilder() {
      return getResponse();
    }

    public static final int ID_FIELD_NUMBER = 2;
    private long id_;
    /**
     * <code>optional uint64 id = 2;</code>
     */
    public long getId() {
      return id_;
    }

    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized == 1) return true;
      if (isInitialized == 0) return false;

      memoizedIsInitialized = 1;
      return true;
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (response_ != null) {
        output.writeMessage(1, getResponse());
      }
      if (id_ != 0L) {
        output.writeUInt64(2, id_);
      }
    }

    public int getSerializedSize() {
      int size = memoizedSize;
      if (size != -1) return size;

      size = 0;
      if (response_ != null) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(1, getResponse());
      }
      if (id_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeUInt64Size(2, id_);
      }
      memoizedSize = size;
      return size;
    }

    private static final long serialVersionUID = 0L;
    @java.lang.Override
    public boolean equals(final java.lang.Object obj) {
      if (obj == this) {
       return true;
      }
      if (!(obj instanceof net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse)) {
        return super.equals(obj);
      }
      net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse other = (net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse) obj;

      boolean result = true;
      result = result && (hasResponse() == other.hasResponse());
      if (hasResponse()) {
        result = result && getResponse()
            .equals(other.getResponse());
      }
      result = result && (getId()
          == other.getId());
      return result;
    }

    @java.lang.Override
    public int hashCode() {
      if (memoizedHashCode != 0) {
        return memoizedHashCode;
      }
      int hash = 41;
      hash = (19 * hash) + getDescriptorForType().hashCode();
      if (hasResponse()) {
        hash = (37 * hash) + RESPONSE_FIELD_NUMBER;
        hash = (53 * hash) + getResponse().hashCode();
      }
      hash = (37 * hash) + ID_FIELD_NUMBER;
      hash = (53 * hash) + com.google.protobuf.Internal.hashLong(
          getId());
      hash = (29 * hash) + unknownFields.hashCode();
      memoizedHashCode = hash;
      return hash;
    }

    public static net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data);
    }
    public static net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return PARSER.parseFrom(data, extensionRegistry);
    }
    public static net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input);
    }
    public static net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input);
    }
    public static net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageV3
          .parseWithIOException(PARSER, input, extensionRegistry);
    }

    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() {
      return this == DEFAULT_INSTANCE
          ? new Builder() : new Builder().mergeFrom(this);
    }

    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    /**
     * Protobuf type {@code proto.PushRateSignalingResponse}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:proto.PushRateSignalingResponse)
        net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponseOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return net.iGap.proto.ProtoPushRateSignaling.internal_static_proto_PushRateSignalingResponse_descriptor;
      }

      protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return net.iGap.proto.ProtoPushRateSignaling.internal_static_proto_PushRateSignalingResponse_fieldAccessorTable
            .ensureFieldAccessorsInitialized(
                net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse.class, net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse.Builder.class);
      }

      // Construct using net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }

      private Builder(
          com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessageV3
                .alwaysUseFieldBuilders) {
        }
      }
      public Builder clear() {
        super.clear();
        if (responseBuilder_ == null) {
          response_ = null;
        } else {
          response_ = null;
          responseBuilder_ = null;
        }
        id_ = 0L;

        return this;
      }

      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return net.iGap.proto.ProtoPushRateSignaling.internal_static_proto_PushRateSignalingResponse_descriptor;
      }

      public net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse getDefaultInstanceForType() {
        return net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse.getDefaultInstance();
      }

      public net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse build() {
        net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }

      public net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse buildPartial() {
        net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse result = new net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse(this);
        if (responseBuilder_ == null) {
          result.response_ = response_;
        } else {
          result.response_ = responseBuilder_.build();
        }
        result.id_ = id_;
        onBuilt();
        return result;
      }

      public Builder clone() {
        return (Builder) super.clone();
      }
      public Builder setField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.setField(field, value);
      }
      public Builder clearField(
          com.google.protobuf.Descriptors.FieldDescriptor field) {
        return (Builder) super.clearField(field);
      }
      public Builder clearOneof(
          com.google.protobuf.Descriptors.OneofDescriptor oneof) {
        return (Builder) super.clearOneof(oneof);
      }
      public Builder setRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          int index, Object value) {
        return (Builder) super.setRepeatedField(field, index, value);
      }
      public Builder addRepeatedField(
          com.google.protobuf.Descriptors.FieldDescriptor field,
          Object value) {
        return (Builder) super.addRepeatedField(field, value);
      }
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse) {
          return mergeFrom((net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }

      public Builder mergeFrom(net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse other) {
        if (other == net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse.getDefaultInstance()) return this;
        if (other.hasResponse()) {
          mergeResponse(other.getResponse());
        }
        if (other.getId() != 0L) {
          setId(other.getId());
        }
        onChanged();
        return this;
      }

      public final boolean isInitialized() {
        return true;
      }

      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse parsedMessage = null;
        try {
          parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
          parsedMessage = (net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse) e.getUnfinishedMessage();
          throw e.unwrapIOException();
        } finally {
          if (parsedMessage != null) {
            mergeFrom(parsedMessage);
          }
        }
        return this;
      }

      private net.iGap.proto.ProtoResponse.Response response_ = null;
      private com.google.protobuf.SingleFieldBuilderV3<
          net.iGap.proto.ProtoResponse.Response, net.iGap.proto.ProtoResponse.Response.Builder, net.iGap.proto.ProtoResponse.ResponseOrBuilder> responseBuilder_;
      /**
       * <code>optional .proto.Response response = 1;</code>
       */
      public boolean hasResponse() {
        return responseBuilder_ != null || response_ != null;
      }
      /**
       * <code>optional .proto.Response response = 1;</code>
       */
      public net.iGap.proto.ProtoResponse.Response getResponse() {
        if (responseBuilder_ == null) {
          return response_ == null ? net.iGap.proto.ProtoResponse.Response.getDefaultInstance() : response_;
        } else {
          return responseBuilder_.getMessage();
        }
      }
      /**
       * <code>optional .proto.Response response = 1;</code>
       */
      public Builder setResponse(net.iGap.proto.ProtoResponse.Response value) {
        if (responseBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          response_ = value;
          onChanged();
        } else {
          responseBuilder_.setMessage(value);
        }

        return this;
      }
      /**
       * <code>optional .proto.Response response = 1;</code>
       */
      public Builder setResponse(
          net.iGap.proto.ProtoResponse.Response.Builder builderForValue) {
        if (responseBuilder_ == null) {
          response_ = builderForValue.build();
          onChanged();
        } else {
          responseBuilder_.setMessage(builderForValue.build());
        }

        return this;
      }
      /**
       * <code>optional .proto.Response response = 1;</code>
       */
      public Builder mergeResponse(net.iGap.proto.ProtoResponse.Response value) {
        if (responseBuilder_ == null) {
          if (response_ != null) {
            response_ =
              net.iGap.proto.ProtoResponse.Response.newBuilder(response_).mergeFrom(value).buildPartial();
          } else {
            response_ = value;
          }
          onChanged();
        } else {
          responseBuilder_.mergeFrom(value);
        }

        return this;
      }
      /**
       * <code>optional .proto.Response response = 1;</code>
       */
      public Builder clearResponse() {
        if (responseBuilder_ == null) {
          response_ = null;
          onChanged();
        } else {
          response_ = null;
          responseBuilder_ = null;
        }

        return this;
      }
      /**
       * <code>optional .proto.Response response = 1;</code>
       */
      public net.iGap.proto.ProtoResponse.Response.Builder getResponseBuilder() {
        
        onChanged();
        return getResponseFieldBuilder().getBuilder();
      }
      /**
       * <code>optional .proto.Response response = 1;</code>
       */
      public net.iGap.proto.ProtoResponse.ResponseOrBuilder getResponseOrBuilder() {
        if (responseBuilder_ != null) {
          return responseBuilder_.getMessageOrBuilder();
        } else {
          return response_ == null ?
              net.iGap.proto.ProtoResponse.Response.getDefaultInstance() : response_;
        }
      }
      /**
       * <code>optional .proto.Response response = 1;</code>
       */
      private com.google.protobuf.SingleFieldBuilderV3<
          net.iGap.proto.ProtoResponse.Response, net.iGap.proto.ProtoResponse.Response.Builder, net.iGap.proto.ProtoResponse.ResponseOrBuilder> 
          getResponseFieldBuilder() {
        if (responseBuilder_ == null) {
          responseBuilder_ = new com.google.protobuf.SingleFieldBuilderV3<
              net.iGap.proto.ProtoResponse.Response, net.iGap.proto.ProtoResponse.Response.Builder, net.iGap.proto.ProtoResponse.ResponseOrBuilder>(
                  getResponse(),
                  getParentForChildren(),
                  isClean());
          response_ = null;
        }
        return responseBuilder_;
      }

      private long id_ ;
      /**
       * <code>optional uint64 id = 2;</code>
       */
      public long getId() {
        return id_;
      }
      /**
       * <code>optional uint64 id = 2;</code>
       */
      public Builder setId(long value) {
        
        id_ = value;
        onChanged();
        return this;
      }
      /**
       * <code>optional uint64 id = 2;</code>
       */
      public Builder clearId() {
        
        id_ = 0L;
        onChanged();
        return this;
      }
      public final Builder setUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }

      public final Builder mergeUnknownFields(
          final com.google.protobuf.UnknownFieldSet unknownFields) {
        return this;
      }


      // @@protoc_insertion_point(builder_scope:proto.PushRateSignalingResponse)
    }

    // @@protoc_insertion_point(class_scope:proto.PushRateSignalingResponse)
    private static final net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse();
    }

    public static net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<PushRateSignalingResponse>
        PARSER = new com.google.protobuf.AbstractParser<PushRateSignalingResponse>() {
      public PushRateSignalingResponse parsePartialFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws com.google.protobuf.InvalidProtocolBufferException {
          return new PushRateSignalingResponse(input, extensionRegistry);
      }
    };

    public static com.google.protobuf.Parser<PushRateSignalingResponse> parser() {
      return PARSER;
    }

    @java.lang.Override
    public com.google.protobuf.Parser<PushRateSignalingResponse> getParserForType() {
      return PARSER;
    }

    public net.iGap.proto.ProtoPushRateSignaling.PushRateSignalingResponse getDefaultInstanceForType() {
      return DEFAULT_INSTANCE;
    }

  }

  private static final com.google.protobuf.Descriptors.Descriptor
    internal_static_proto_PushRateSignalingResponse_descriptor;
  private static final 
    com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
      internal_static_proto_PushRateSignalingResponse_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static  com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\027PushRateSignaling.proto\022\005proto\032\016Respon" +
      "se.proto\"J\n\031PushRateSignalingResponse\022!\n" +
      "\010response\030\001 \001(\0132\017.proto.Response\022\n\n\002id\030\002" +
      " \001(\004B(\n\016net.iGap.protoB\026ProtoPushRateSig" +
      "nalingb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          net.iGap.proto.ProtoResponse.getDescriptor(),
        }, assigner);
    internal_static_proto_PushRateSignalingResponse_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_proto_PushRateSignalingResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessageV3.FieldAccessorTable(
        internal_static_proto_PushRateSignalingResponse_descriptor,
        new java.lang.String[] { "Response", "Id", });
    net.iGap.proto.ProtoResponse.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}
