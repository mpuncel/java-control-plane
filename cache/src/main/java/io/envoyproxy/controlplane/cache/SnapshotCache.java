package io.envoyproxy.controlplane.cache;

public interface SnapshotCache<Group, Response> extends Cache<Group, Response> {

  /**
   * Clears the most recently set {@link Snapshot} and associated metadata for the given node group.
   *
   * <p>Should only be called when the {@link Snapshot} will no longer be needed, e.g. when there
   * are no open watches for the group.
   *
   * <p>Implementations are free to ignore this call should it possibly leave the cache in a bad
   * state, e.g. causing watches to hang waiting for {@link Snapshot} options that will never happen.
   *
   * @param group group identifier
   * @return true if the snapshot was cleared, false otherwise
   */
  boolean clearSnapshot(Group group);

  /**
   * Returns the most recently set {@link Snapshot} for the given node group.
   *
   * @param group group identifier
   * @return latest snapshot
   */
  Snapshot getSnapshot(Group group);

  /**
   * Set the {@link Snapshot} for the given node group. Snapshots should have distinct versions and be internally
   * consistent (i.e. all referenced resources must be included in the snapshot).
   *
   * @param group group identifier
   * @param snapshot a versioned collection of node config data
   */
  void setSnapshot(Group group, Snapshot snapshot);
}
