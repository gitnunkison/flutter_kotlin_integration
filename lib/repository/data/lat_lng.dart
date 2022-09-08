class LatLng {
  final double lat;
  final double lng;

  LatLng({
    required this.lat,
    required this.lng,
  });

  static LatLng empty() => LatLng(
        lat: 0.0,
        lng: 0.0,
      );

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is LatLng &&
          runtimeType == other.runtimeType &&
          lat == other.lat &&
          lng == other.lng;

  @override
  int get hashCode => lat.hashCode ^ lng.hashCode;
}
