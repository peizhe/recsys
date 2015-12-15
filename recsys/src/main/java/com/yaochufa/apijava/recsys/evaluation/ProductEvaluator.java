package com.yaochufa.apijava.recsys.evaluation;

import java.io.IOException;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import com.yaochufa.apijava.recsys.util.SpringContextHelper;

public class ProductEvaluator
{

	final static int NEIGHBORHOOD_NUM = 2;
	final static int RECOMMENDER_NUM = 3;

	public static void main(String[] args) throws TasteException, IOException
	{
//		 String file = "datafile/book/rating.csv";
//		SpringContextHelper.init();
//		MySQLJDBCDataModel dataModel = SpringContextHelper
//				.getBean("cfDataModel", MySQLJDBCDataModel.class);
		 String file = "data/csv/order/taste_preferences.txt";
		 DataModel dataModel = RecommendFactory.buildDataModel(file);
//		userEuclidean(dataModel);
		// userLoglikelihood(dataModel);
		// userEuclideanNoPref(dataModel);
		 itemEuclidean(dataModel);
//		 itemLoglikelihood(dataModel);
//		 itemEuclideanNoPref(dataModel);
	}

	public static RecommenderBuilder userEuclidean(DataModel dataModel)
			throws TasteException, IOException
	{
		System.out.println("userEuclidean");
		UserSimilarity userSimilarity = RecommendFactory.userSimilarity(
				RecommendFactory.SIMILARITY.EUCLIDEAN, dataModel);
		UserNeighborhood userNeighborhood = RecommendFactory.userNeighborhood(
				RecommendFactory.NEIGHBORHOOD.NEAREST, userSimilarity,
				dataModel, NEIGHBORHOOD_NUM);
		RecommenderBuilder recommenderBuilder = RecommendFactory
				.userRecommender(userSimilarity, userNeighborhood, true);

		RecommendFactory.evaluate(
				RecommendFactory.EVALUATOR.AVERAGE_ABSOLUTE_DIFFERENCE,
				recommenderBuilder, null, dataModel, 0.4);
		RecommendFactory.statsEvaluator(recommenderBuilder, null, dataModel, 2);
		return recommenderBuilder;
	}

	public static RecommenderBuilder userLoglikelihood(DataModel dataModel)
			throws TasteException, IOException
	{
		System.out.println("userLoglikelihood");
		UserSimilarity userSimilarity = RecommendFactory.userSimilarity(
				RecommendFactory.SIMILARITY.LOGLIKELIHOOD, dataModel);
		UserNeighborhood userNeighborhood = RecommendFactory.userNeighborhood(
				RecommendFactory.NEIGHBORHOOD.NEAREST, userSimilarity,
				dataModel, NEIGHBORHOOD_NUM);
		RecommenderBuilder recommenderBuilder = RecommendFactory
				.userRecommender(userSimilarity, userNeighborhood, true);

		RecommendFactory.evaluate(
				RecommendFactory.EVALUATOR.AVERAGE_ABSOLUTE_DIFFERENCE,
				recommenderBuilder, null, dataModel, 0.7);
		RecommendFactory.statsEvaluator(recommenderBuilder, null, dataModel, 2);
		return recommenderBuilder;
	}

	public static RecommenderBuilder userEuclideanNoPref(DataModel dataModel)
			throws TasteException, IOException
	{
		System.out.println("userEuclideanNoPref");
		UserSimilarity userSimilarity = RecommendFactory.userSimilarity(
				RecommendFactory.SIMILARITY.EUCLIDEAN, dataModel);
		UserNeighborhood userNeighborhood = RecommendFactory.userNeighborhood(
				RecommendFactory.NEIGHBORHOOD.NEAREST, userSimilarity,
				dataModel, NEIGHBORHOOD_NUM);
		RecommenderBuilder recommenderBuilder = RecommendFactory
				.userRecommender(userSimilarity, userNeighborhood, false);

		RecommendFactory.evaluate(
				RecommendFactory.EVALUATOR.AVERAGE_ABSOLUTE_DIFFERENCE,
				recommenderBuilder, null, dataModel, 0.95);
		RecommendFactory.statsEvaluator(recommenderBuilder, null, dataModel, 2);
		return recommenderBuilder;
	}

	public static RecommenderBuilder itemEuclidean(DataModel dataModel)
			throws TasteException, IOException
	{
		System.out.println("itemEuclidean");
		ItemSimilarity itemSimilarity = RecommendFactory.itemSimilarity(
				RecommendFactory.SIMILARITY.EUCLIDEAN, dataModel);
		RecommenderBuilder recommenderBuilder = RecommendFactory
				.itemRecommender(itemSimilarity, true);

		RecommendFactory.evaluate(
				RecommendFactory.EVALUATOR.AVERAGE_ABSOLUTE_DIFFERENCE,
				recommenderBuilder, null, dataModel, 0.7);
		RecommendFactory.statsEvaluator(recommenderBuilder, null, dataModel, 2);
		return recommenderBuilder;
	}

	public static RecommenderBuilder itemLoglikelihood(DataModel dataModel)
			throws TasteException, IOException
	{
		System.out.println("itemLoglikelihood");
		ItemSimilarity itemSimilarity = RecommendFactory.itemSimilarity(
				RecommendFactory.SIMILARITY.LOGLIKELIHOOD, dataModel);
		RecommenderBuilder recommenderBuilder = RecommendFactory
				.itemRecommender(itemSimilarity, true);

		RecommendFactory.evaluate(
				RecommendFactory.EVALUATOR.AVERAGE_ABSOLUTE_DIFFERENCE,
				recommenderBuilder, null, dataModel, 0.7);
		RecommendFactory.statsEvaluator(recommenderBuilder, null, dataModel, 2);
		return recommenderBuilder;
	}

	public static RecommenderBuilder itemEuclideanNoPref(DataModel dataModel)
			throws TasteException, IOException
	{
		System.out.println("itemEuclideanNoPref");
		ItemSimilarity itemSimilarity = RecommendFactory.itemSimilarity(
				RecommendFactory.SIMILARITY.EUCLIDEAN, dataModel);
		RecommenderBuilder recommenderBuilder = RecommendFactory
				.itemRecommender(itemSimilarity, false);

		RecommendFactory.evaluate(
				RecommendFactory.EVALUATOR.AVERAGE_ABSOLUTE_DIFFERENCE,
				recommenderBuilder, null, dataModel, 0.7);
		RecommendFactory.statsEvaluator(recommenderBuilder, null, dataModel, 2);
		return recommenderBuilder;
	}

}
