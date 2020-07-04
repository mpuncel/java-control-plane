package io.envoyproxy.controlplane.server.callback;

import io.envoyproxy.controlplane.cache.NodeGroup;
import io.envoyproxy.controlplane.cache.SnapshotCache;
import io.envoyproxy.envoy.api.v2.DiscoveryRequest;
import io.envoyproxy.envoy.api.v2.DiscoveryResponse;
import io.envoyproxy.envoy.api.v2.core.Node;
import java.time.Clock;
import java.util.Set;
import java.util.function.Consumer;

public class V2SnapshotCollectingCallback<T>
    extends SnapshotCollectingCallback<T, DiscoveryRequest, DiscoveryResponse, Node> {

  public V2SnapshotCollectingCallback(SnapshotCache<T> snapshotCache,
      NodeGroup<T, Node> nodeGroup, Clock clock, Set<Consumer<T>> collectorCallbacks,
      long collectAfterMillis, long collectionIntervalMillis) {
    super(snapshotCache, nodeGroup, clock, collectorCallbacks, collectAfterMillis,
        collectionIntervalMillis);
  }

  @Override Node getNode(DiscoveryRequest xdsRequest) {
    return xdsRequest.getNode();
  }
}
