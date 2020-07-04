package io.envoyproxy.controlplane.cache;

import io.envoyproxy.envoy.api.v2.core.Node;

public class V2SimpleCache<T> extends SimpleCache<T, Node> {
  public V2SimpleCache(NodeGroup<T, Node> nodeGroup) {
    super(nodeGroup);
  }

  @Override
  Node getNode(XdsRequest request) {
    return request.v2Request().getNode();
  }
}
