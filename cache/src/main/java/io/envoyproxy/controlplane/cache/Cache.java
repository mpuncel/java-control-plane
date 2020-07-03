package io.envoyproxy.controlplane.cache;

import java.util.Collection;
import javax.annotation.concurrent.ThreadSafe;

/**
 * {@code Cache} is a generic config cache with support for watchers.
 */
@ThreadSafe
public interface Cache<Group, Response> extends ConfigWatcher<Response> {

  /**
   * Returns all known groups.
   *
   */
  Collection<Group> groups();

  /**
   * Returns the current {@link StatusInfo} for the given group.
   *
   * @param group the node group whose status is being fetched
   */
  StatusInfo statusInfo(Group group);
}
