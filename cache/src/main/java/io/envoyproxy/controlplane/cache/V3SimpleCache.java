package io.envoyproxy.controlplane.cache;

import io.envoyproxy.envoy.config.core.v3.Node;

public class V3SimpleCache<T> extends SimpleCache<T, Node> {
  public V3SimpleCache(NodeGroup<T, Node> nodeGroup) {
    super(nodeGroup);
  }

  @Override
  Node getNode(XdsRequest request) {
    return request.v3Request().getNode();
  }
}
