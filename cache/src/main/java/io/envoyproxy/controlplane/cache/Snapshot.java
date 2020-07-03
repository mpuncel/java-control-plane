package io.envoyproxy.controlplane.cache;

import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.protobuf.Message;
import io.envoyproxy.envoy.api.v2.Cluster;
import io.envoyproxy.envoy.api.v2.ClusterLoadAssignment;
import io.envoyproxy.envoy.api.v2.Listener;
import io.envoyproxy.envoy.api.v2.RouteConfiguration;
import io.envoyproxy.envoy.api.v2.auth.Secret;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@code Snapshot} is a data class that contains an internally consistent snapshot of xDS resources. Snapshots should
 * have distinct versions per node group.
 */
@AutoValue
public abstract class Snapshot<C, CLA, L, R, S> {

  /**
   * Returns a new {@link Snapshot} instance that is versioned uniformly across all resources.
   *
   * @param clusters the cluster resources in this snapshot
   * @param endpoints the endpoint resources in this snapshot
   * @param listeners the listener resources in this snapshot
   * @param routes the route resources in this snapshot
   * @param version the version associated with all resources in this snapshot
   */
  public static Snapshot create(
      Iterable<Cluster> clusters,
      Iterable<ClusterLoadAssignment> endpoints,
      Iterable<Listener> listeners,
      Iterable<RouteConfiguration> routes,
      Iterable<Secret> secrets,
      String version) {

    return new AutoValue_Snapshot(
        SnapshotResources.create(clusters, version),
        null,
        SnapshotResources.create(endpoints, version),
        null,
        SnapshotResources.create(listeners, version),
        null,
        SnapshotResources.create(routes, version),
        null,
        SnapshotResources.create(secrets, version),
        null);
  }

  /**
   * Returns a new {@link Snapshot} instance that is versioned uniformly across all resources.
   *
   * @param clusters the cluster resources in this snapshot
   * @param endpoints the endpoint resources in this snapshot
   * @param listeners the listener resources in this snapshot
   * @param routes the route resources in this snapshot
   * @param version the version associated with all resources in this snapshot
   */
  public static Snapshot createV3(
      Iterable<io.envoyproxy.envoy.config.cluster.v3.Cluster> clusters,
      Iterable<io.envoyproxy.envoy.config.endpoint.v3.ClusterLoadAssignment> endpoints,
      Iterable<io.envoyproxy.envoy.config.listener.v3.Listener> listeners,
      Iterable<io.envoyproxy.envoy.config.route.v3.RouteConfiguration> routes,
      Iterable<io.envoyproxy.envoy.extensions.transport_sockets.tls.v3.Secret> secrets,
      String version) {

    return new AutoValue_Snapshot(
        null,
        SnapshotResources.create(clusters, version),
        null,
        SnapshotResources.create(endpoints, version),
        null,
        SnapshotResources.create(listeners, version),
        null,
        SnapshotResources.create(routes, version),
        null,
        SnapshotResources.create(secrets, version));
  }

  /**
   * Returns a new {@link Snapshot} instance that has separate versions for each resource type.
   *
   * @param clusters the cluster resources in this snapshot
   * @param clustersVersion the version of the cluster resources
   * @param endpoints the endpoint resources in this snapshot
   * @param endpointsVersion the version of the endpoint resources
   * @param listeners the listener resources in this snapshot
   * @param listenersVersion the version of the listener resources
   * @param routes the route resources in this snapshot
   * @param routesVersion the version of the route resources
   */
  public static Snapshot create(
      Iterable<Cluster> clusters,
      String clustersVersion,
      Iterable<ClusterLoadAssignment> endpoints,
      String endpointsVersion,
      Iterable<Listener> listeners,
      String listenersVersion,
      Iterable<RouteConfiguration> routes,
      String routesVersion,
      Iterable<Secret> secrets,
      String secretsVersion) {

    // TODO(snowp): add a builder alternative
    return new AutoValue_Snapshot(
        SnapshotResources.create(clusters, clustersVersion),
        null,
        SnapshotResources.create(endpoints, endpointsVersion),
        null,
        SnapshotResources.create(listeners, listenersVersion),
        null,
        SnapshotResources.create(routes, routesVersion),
        null,
        SnapshotResources.create(secrets, secretsVersion),
        null);
  }

  /**
   * Returns a new {@link Snapshot} instance that has separate versions for each resource type.
   *
   * @param clusters the cluster resources in this snapshot
   * @param clusterVersionResolver version resolver of the clusters in this snapshot
   * @param endpoints the endpoint resources in this snapshot
   * @param endpointVersionResolver version resolver of the endpoints in this snapshot
   * @param listeners the listener resources in this snapshot
   * @param listenerVersionResolver version resolver of listeners in this snapshot
   * @param routes the route resources in this snapshot
   * @param routeVersionResolver version resolver of the routes in this snapshot
   * @param secrets the secret resources in this snapshot
   * @param secretVersionResolver version resolver of the secrets in this snapshot
   */
  public static Snapshot create(
      Iterable<Cluster> clusters,
      ResourceVersionResolver clusterVersionResolver,
      Iterable<ClusterLoadAssignment> endpoints,
      ResourceVersionResolver endpointVersionResolver,
      Iterable<Listener> listeners,
      ResourceVersionResolver listenerVersionResolver,
      Iterable<RouteConfiguration> routes,
      ResourceVersionResolver routeVersionResolver,
      Iterable<Secret> secrets,
      ResourceVersionResolver secretVersionResolver) {

    return new AutoValue_Snapshot(
        SnapshotResources.create(clusters, clusterVersionResolver),
        null,
        SnapshotResources.create(endpoints, endpointVersionResolver),
        null,
        SnapshotResources.create(listeners, listenerVersionResolver),
        null,
        SnapshotResources.create(routes, routeVersionResolver),
        null,
        SnapshotResources.create(secrets, secretVersionResolver),
        null);
  }

  /**
   * Creates an empty snapshot with the given version.
   *
   * @param version the version of the snapshot resources
   */
  public static Snapshot createEmpty(String version) {
    return create(Collections.emptySet(), Collections.emptySet(),
        Collections.emptySet(), Collections.emptySet(), Collections.emptySet(), version);
  }

  /**
   * Returns all cluster items in the CDS payload.
   */
  public abstract SnapshotResources<Cluster> clusters();

  /**
   * Returns all v3 cluster items in the CDS payload.
   */
  public abstract SnapshotResources<io.envoyproxy.envoy.config.cluster.v3.Cluster> clustersV3();

  /**
   * Returns all endpoint items in the EDS payload.
   */
  public abstract SnapshotResources<ClusterLoadAssignment> endpoints();

  /**
   * Returns all v3 endpoint items in the EDS payload.
   */
  public abstract SnapshotResources<io.envoyproxy.envoy.config.endpoint.v3.ClusterLoadAssignment> endpointsV3();

  /**
   * Returns all listener items in the LDS payload.
   */
  public abstract SnapshotResources<Listener> listeners();

  /**
   * Returns all listener items in the LDS payload.
   */
  public abstract SnapshotResources<io.envoyproxy.envoy.config.listener.v3.Listener> listenersV3();

  /**
   * Returns all route items in the RDS payload.
   */
  public abstract SnapshotResources<RouteConfiguration> routes();

  /**
   * Returns all route items in the RDS payload.
   */
  public abstract SnapshotResources<io.envoyproxy.envoy.config.route.v3.RouteConfiguration> routesV3();

  /**
   * Returns all secret items in the SDS payload.
   */
  public abstract SnapshotResources<Secret> secrets();

  /**
   * Returns all secret items in the SDS payload.
   */
  public abstract SnapshotResources<io.envoyproxy.envoy.extensions.transport_sockets.tls.v3.Secret> secretsV3();

  /**
   * Asserts that all dependent resources are included in the snapshot. All EDS resources are listed by name in CDS
   * resources, and all RDS resources are listed by name in LDS resources.
   *
   * <p>Note that clusters and listeners are requested without name references, so Envoy will accept the snapshot list
   * of clusters as-is, even if it does not match all references found in xDS.
   *
   * @throws SnapshotConsistencyException if the snapshot is not consistent
   */
  public void ensureConsistent() throws SnapshotConsistencyException {
    Set<String> clusterEndpointRefs = Resources.getResourceReferences(clusters().resources().values());

    ensureAllResourceNamesExist(Resources.CLUSTER_TYPE_URL, Resources.ENDPOINT_TYPE_URL, clusterEndpointRefs, endpoints().resources());

    Set<String> v3ClusterEndpointRefs =
        Resources.getResourceReferences(clustersV3().resources().values());

    ensureAllResourceNamesExist(Resources.V3_CLUSTER_TYPE_URL, Resources.V3_ENDPOINT_TYPE_URL, v3ClusterEndpointRefs,
        endpointsV3().resources());

    Set<String> listenerRouteRefs = Resources.getResourceReferences(listeners().resources().values());

    ensureAllResourceNamesExist(Resources.LISTENER_TYPE_URL, Resources.ROUTE_TYPE_URL, listenerRouteRefs, routes().resources());

    Set<String> v3ListenerRouteRefs =
        Resources.getResourceReferences(listenersV3().resources().values());

    ensureAllResourceNamesExist(Resources.V3_LISTENER_TYPE_URL, Resources.V3_ROUTE_TYPE_URL, v3ListenerRouteRefs,
        routesV3().resources());
  }

  /**
   * Returns the resources with the given type.
   *
   * @param typeUrl the URL for the requested resource type
   */
  public Map<String, ? extends Message> resources(String typeUrl) {
    if (Strings.isNullOrEmpty(typeUrl)) {
      return ImmutableMap.of();
    }

    switch (typeUrl) {
      case Resources.CLUSTER_TYPE_URL:
        return clusters().resources();
      case Resources.V3_CLUSTER_TYPE_URL:
        return clustersV3().resources();
      case Resources.ENDPOINT_TYPE_URL:
        return endpoints().resources();
      case Resources.V3_ENDPOINT_TYPE_URL:
        return endpointsV3().resources();
      case Resources.LISTENER_TYPE_URL:
        return listeners().resources();
      case Resources.V3_LISTENER_TYPE_URL:
        return listenersV3().resources();
      case Resources.ROUTE_TYPE_URL:
        return routes().resources();
      case Resources.V3_ROUTE_TYPE_URL:
        return routesV3().resources();
      case Resources.SECRET_TYPE_URL:
        return secrets().resources();
      case Resources.V3_SECRET_TYPE_URL:
        return secretsV3().resources();
      default:
        return ImmutableMap.of();
    }
  }

  /**
   * Returns the version in this snapshot for the given resource type.
   *
   * @param typeUrl the URL for the requested resource type
   */
  public String version(String typeUrl) {
    return version(typeUrl, Collections.emptyList());
  }

  /**
   * Returns the version in this snapshot for the given resource type.
   *
   * @param typeUrl the URL for the requested resource type
   * @param resourceNames list of requested resource names,
   *                      used to calculate a version for the given resources
   */
  public String version(String typeUrl, List<String> resourceNames) {
    if (Strings.isNullOrEmpty(typeUrl)) {
      return "";
    }

    switch (typeUrl) {
      case Resources.CLUSTER_TYPE_URL:
        return clusters().version(resourceNames);
      case Resources.V3_CLUSTER_TYPE_URL:
        return clustersV3().version(resourceNames);
      case Resources.ENDPOINT_TYPE_URL:
        return endpoints().version(resourceNames);
      case Resources.V3_ENDPOINT_TYPE_URL:
        return endpointsV3().version(resourceNames);
      case Resources.LISTENER_TYPE_URL:
        return listeners().version(resourceNames);
      case Resources.V3_LISTENER_TYPE_URL:
        return listenersV3().version(resourceNames);
      case Resources.ROUTE_TYPE_URL:
        return routes().version(resourceNames);
      case Resources.V3_ROUTE_TYPE_URL:
        return routesV3().version(resourceNames);
      case Resources.SECRET_TYPE_URL:
        return secrets().version(resourceNames);
      case Resources.V3_SECRET_TYPE_URL:
        return secretsV3().version(resourceNames);
      default:
        return "";
    }
  }

  /**
   * Asserts that all of the given resource names have corresponding values in the given resources collection.
   *
   * @param parentTypeUrl the type of the parent resources (source of the resource name refs)
   * @param dependencyTypeUrl the type of the given dependent resources
   * @param resourceNames the set of dependent resource names that must exist
   * @param resources the collection of resources whose names are being checked
   * @throws SnapshotConsistencyException if a name is given that does not exist in the resources collection
   */
  private static void ensureAllResourceNamesExist(
      String parentTypeUrl,
      String dependencyTypeUrl,
      Set<String> resourceNames,
      Map<String, ? extends Message> resources) throws SnapshotConsistencyException {

    if (resourceNames.size() != resources.size()) {
      throw new SnapshotConsistencyException(
          String.format(
              "Mismatched %s -> %s reference and resource lengths, [%s] != %d",
              parentTypeUrl,
              dependencyTypeUrl,
              String.join(", ", resourceNames),
              resources.size()));
    }

    for (String name : resourceNames) {
      if (!resources.containsKey(name)) {
        throw new SnapshotConsistencyException(
            String.format(
                "%s named '%s', referenced by a %s, not listed in [%s]",
                dependencyTypeUrl,
                name,
                parentTypeUrl,
                String.join(", ", resources.keySet())));
      }
    }
  }

}
