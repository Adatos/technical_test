// Code to work with Google Earth Engine

// Creating the AOI and BaseMap variables
var AOI = ee.FeatureCollection('users/hafizmagnus/AOI');
var bm_extent = ee.FeatureCollection('users/hafizmagnus/Basemap_Extent');

// Looking of the least cloudy SENTINEL-2 image within the last 3 months
var ST2 = ee.ImageCollection('COPERNICUS/S2')
var image = ee.Image(
  ST2.filterBounds(AOI)
    .filterDate('2017-11-15', '2018-02-19')
    .sort('CLOUD_COVERAGE_ASSESSMENT')
    .first()
	);

// Looking at the details of the best image
print(image, "Image Details");	
	
// Getting the Normalized Difference Vegetation Index (NDVI)
var ndvi = image.normalizedDifference(['B8', 'B4']).rename('NDVI');

// Getting the Nitrogen Reflectance Index
var nri = image.normalizedDifference(['B3', 'B4']).rename('NRI');

// Display the NDVI data on the map
var ndviParams = {min: -1, max: 1, palette: ['blue', 'white', 'green']};
Map.addLayer(ndvi, ndviParams, 'NDVI image');

// Display the NRI on the map
var nriParams = {min: -1, max: 1, palette: ['yellow', 'white', 'red']};
Map.addLayer(nri, nriParams, 'NRI image');

// Display the true-color image
var visParams = {bands: ['B4', 'B3', 'B2'], max: 4000};
Map.addLayer(image, visParams, 'RGB image');

// Adding the AOI and BaseMap extent to the map
Map.addLayer(AOI, {color: 'red'}, 'Area of Interest');
Map.addLayer(bm_extent, {color: 'gray'}, 'Basemap Extent');

// Centering the map to the Area of Interest
Map.centerObject(AOI)

// Clipping the RBG image to the BaseMap extent and saving the data in the drive
Export.image.toDrive({
	image: image.float(),
	description: 'RBG_Image',
	scale: 10,
	maxPixels: 100000,
	region: bm_extent}
	);

// Clipping the NDVI image to the AOI extent and saving the data in the drive
Export.image.toDrive({
	image: ndvi.float(),
	description: 'NDVI_Image',
	scale: 10,
	maxPixels: 100000,
	region: AOI}
	);	

// Clipping the NRI image to the AOI extent and saving the data in the drive
Export.image.toDrive({
	image: nri.float(),
	description: 'NRI_Image',
	scale: 10,
	maxPixels: 100000,
	region: AOI}
	);		
/////////////////////
