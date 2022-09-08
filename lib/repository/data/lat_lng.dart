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
}
