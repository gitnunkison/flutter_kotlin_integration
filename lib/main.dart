import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'dart:developer' as developer;

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'FlutterKotlinIntegration',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Flutter -> Kotlin Integration'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const platform = MethodChannel('com.nunkison.flutter/location');
  var _lat = 0.0;
  var _lng = 0.0;

  Future<void> _getLocation() async {
    try {
      final location = await platform.invokeMethod('getLocation');
      setState(() {
        _lat = location[0];
        _lng = location[1];
      });
    } on PlatformException catch (e) {
      developer.log(e.stacktrace.toString());
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text(e.message ?? "Erro ao capturar a localização"),
        ),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text(
              'Minha Localização',
            ),
            Text(
              "LAT: $_lat",
              style: Theme.of(context).textTheme.headline4,
            ),
            Text(
              "LNG: $_lng",
              style: Theme.of(context).textTheme.headline4,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _getLocation,
        tooltip: 'Get Location',
        child: const Icon(Icons.gps_fixed),
      ),
    );
  }
}
