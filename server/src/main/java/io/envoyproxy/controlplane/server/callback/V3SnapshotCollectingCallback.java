package io.envoyproxy.controlplane.server.callback;

import io.envoyproxy.controlplane.cache.NodeGroup;
import io.envoyproxy.controlplane.cache.SnapshotCache;
import io.envoyproxy.envoy.config.core.v3.Node;
import io.envoyproxy.envoy.service.discovery.v3.DiscoveryRequest;
import io.envoyproxy.envoy.service.discovery.v3.DiscoveryResponse;
import java.time.Clock;
import java.util.Set;
import java.util.function.Consumer;

public class V3SnapshotCollectingCallback<T>
    extends SnapshotCollectingCallback<T, DiscoveryRequest, DiscoveryResponse, Node>  {

  public V3SnapshotCollectingCallback(SnapshotCache<T> snapshotCache,
        NodeGroup<T, Node> nodeGroup, Clock clock, Set<Consumer<T>> collectorCallbacks,
    long collectAfterMillis, long collectionIntervalMillis) {
      super(snapshotCache, nodeGroup, clock, collectorCallbacks, collectAfterMillis,
          collectionIntervalMillis);
    }

    @Override Node getNode(DiscoveryRequest xdsRequest) {
      return xdsRequest.getNode();
    }
}
