package com.github.ambry.shared;

import com.github.ambry.clustermap.ClusterMap;
import com.github.ambry.clustermap.PartitionId;
import com.github.ambry.store.FindToken;
import com.github.ambry.store.FindTokenFactory;
import com.github.ambry.utils.Utils;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;


/**
 * Contains the token, hostname, replicapath for a local partition. This is used
 * by replica metadata request to specify token in a partition
 */
public class ReplicaMetadataRequestInfo {
  private FindToken token;
  private String hostName;
  private String replicaPath;
  private PartitionId partitionId;

  private static final int ReplicaPath_Field_Size_In_Bytes = 4;
  private static final int HostName_Field_Size_In_Bytes = 4;

  public ReplicaMetadataRequestInfo(PartitionId partitionId, FindToken token, String hostName, String replicaPath) {
    this.partitionId = partitionId;
    this.token = token;
    this.hostName = hostName;
    this.replicaPath = replicaPath;
  }

  public static ReplicaMetadataRequestInfo readFrom(DataInputStream stream, ClusterMap clusterMap,
      FindTokenFactory factory)
      throws IOException {
    String hostName = Utils.readIntString(stream);
    String replicaPath = Utils.readIntString(stream);
    PartitionId partitionId = clusterMap.getPartitionIdFromStream(stream);
    FindToken token = factory.getFindToken(stream);
    return new ReplicaMetadataRequestInfo(partitionId, token, hostName, replicaPath);
  }

  public void writeTo(ByteBuffer buffer) {
    buffer.putInt(hostName.getBytes().length);
    buffer.put(hostName.getBytes());
    buffer.putInt(replicaPath.getBytes().length);
    buffer.put(replicaPath.getBytes());
    buffer.put(partitionId.getBytes());
    buffer.put(token.toBytes());
  }

  public long sizeInBytes() {
    return HostName_Field_Size_In_Bytes + hostName.getBytes().length + ReplicaPath_Field_Size_In_Bytes + replicaPath
        .getBytes().length +
        +partitionId.getBytes().length + token.toBytes().length;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Token=").append(token);
    sb.append(", ").append("PartitionId=").append(partitionId);
    sb.append(", ").append("HostName=").append(hostName);
    sb.append(", ").append("ReplicaPath=").append(replicaPath);
    return sb.toString();
  }

  public FindToken getToken() {
    return token;
  }

  public String getHostName() {
    return this.hostName;
  }

  public String getReplicaPath() {
    return this.replicaPath;
  }

  public PartitionId getPartitionId() {
    return partitionId;
  }
}
