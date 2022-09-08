import 'package:flutter/material.dart';

import '../repository/data/lat_lng.dart';

class HomeScreen extends StatelessWidget {
  final LatLng latLng;
  final String? message;
  final bool loading;

  const HomeScreen({
    Key? key,
    required this.latLng,
    required this.loading,
    this.message,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: <Widget>[
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: SizedBox(
              width: 50,
              height: 50,
              child: loading ? const CircularProgressIndicator() : Container(),
            ),
          ),
          const Text(
            'Minha Localização',
          ),
          Text(
            "LAT: ${latLng.lat}",
            style: Theme.of(context).textTheme.headline4,
          ),
          Text(
            "LNG: ${latLng.lng}",
            style: Theme.of(context).textTheme.headline4,
          ),
          (message != null)
              ? Text(
                  "Mensagem: $message",
                )
              : Container(),
        ],
      ),
    );
  }
}
