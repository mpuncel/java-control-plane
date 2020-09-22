package io.envoyproxy.controlplane.cache;

import io.envoyproxy.envoy.api.v2.core.Node;
import java.util.LinkedList;
import java.util.function.Consumer;

public class TestUtils {
  public static class ResponseTracker implements Consumer<Response> {

    public final LinkedList<Response> responses = new LinkedList<>();

    @Override
    public void accept(Response response) {
      responses.add(response);
    }

  }

  public static class ResponseOrderTracker implements Consumer<Response> {

    public final LinkedList<String> responseTypes = new LinkedList<>();

    @Override public void accept(Response response) {
      responseTypes.add(response.request().getTypeUrl());
    }
  }

  public static class SingleNodeGroup implements NodeGroup<String> {

    public static final String GROUP = "node";

    @Override
    public String hash(Node node) {
      if (node == null) {
        throw new IllegalArgumentException("node");
      }

      return GROUP;
    }

    @Override
    public String hash(io.envoyproxy.envoy.config.core.v3.Node node) {
      throw new IllegalStateException("should not have received a v3 Node in a v2 Test");
    }
  }

  public static class WatchAndTracker {

    public final Watch watch;
    public final ResponseTracker tracker;

    public WatchAndTracker(Watch watch, ResponseTracker tracker) {
      this.watch = watch;
      this.tracker = tracker;
    }
  }
}
