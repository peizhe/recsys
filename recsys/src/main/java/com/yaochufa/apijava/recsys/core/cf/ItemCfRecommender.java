/*
 * Source code for listing 5.5
 * 
 */
package com.yaochufa.apijava.recsys.core.cf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;

import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.IDRescorer;
import org.apache.mahout.cf.taste.recommender.ItemBasedRecommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Rescorer;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.common.LongPair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;
import com.yaochufa.apijava.recsys.mahout.GenderRescorer;

@Component
public class ItemCfRecommender implements ItemBasedRecommender
{

	private final ItemBasedRecommender delegate;
	private final DataModel model;
	private final FastIDSet men;
	private final FastIDSet women;
	private final FastIDSet usersRateMoreMen;
	private final FastIDSet usersRateLessMen;

	public ItemCfRecommender() throws TasteException, IOException
	{
		this(new FileDataModel(readResourceToTempFile("data/libimseti/ratings.dat")));
	}

	@Autowired
	public ItemCfRecommender(DataModel model) throws TasteException, IOException
	{
		ItemSimilarity similarity = new EuclideanDistanceSimilarity(model);
		delegate = new GenericItemBasedRecommender(model, similarity);
		this.model = model;
		FastIDSet[] menWomen = GenderRescorer
				.parseMenWomen(readResourceToTempFile("data/libimseti/gender.dat"));
		men = menWomen[0];
		women = menWomen[1];
		usersRateMoreMen = new FastIDSet(50000);
		usersRateLessMen = new FastIDSet(50000);
	}

	@Override
	public List<RecommendedItem> recommend(long userID, int howMany)
			throws TasteException
	{
		IDRescorer rescorer = new GenderRescorer(men, women, usersRateMoreMen,
				usersRateLessMen, userID, model);
		return delegate.recommend(userID, howMany, rescorer);
	}

	@Override
	public List<RecommendedItem> recommend(long userID, int howMany,
			IDRescorer rescorer) throws TasteException
	{
		return delegate.recommend(userID, howMany, rescorer);
	}

	@Override
	public float estimatePreference(long userID, long itemID)
			throws TasteException
	{
		IDRescorer rescorer = new GenderRescorer(men, women, usersRateMoreMen,
				usersRateLessMen, userID, model);
		return (float) rescorer.rescore(itemID,
				delegate.estimatePreference(userID, itemID));
	}

	@Override
	public void setPreference(long userID, long itemID, float value)
			throws TasteException
	{
		delegate.setPreference(userID, itemID, value);
	}

	@Override
	public void removePreference(long userID, long itemID) throws TasteException
	{
		delegate.removePreference(userID, itemID);
	}

	@Override
	public DataModel getDataModel()
	{
		return delegate.getDataModel();
	}

	@Override
	public void refresh(Collection<Refreshable> alreadyRefreshed)
	{
		delegate.refresh(alreadyRefreshed);
	}

	static File readResourceToTempFile(String resourceName) throws IOException
	{
		String absoluteResource = resourceName.startsWith("/") ? resourceName
				: '/' + resourceName;
		InputSupplier<? extends InputStream> inSupplier;
		try
		{
			URL resourceURL = Resources.getResource(ItemCfRecommender.class,
					absoluteResource);
			inSupplier = Resources.newInputStreamSupplier(resourceURL);
		} catch (IllegalArgumentException iae)
		{
			File resourceFile = new File(resourceName);
			inSupplier = Files.newInputStreamSupplier(resourceFile);
		}
		File tempFile = File.createTempFile("taste", null);
		tempFile.deleteOnExit();
		Files.copy(inSupplier, tempFile);
		return tempFile;
	}

	@Override
	public List<RecommendedItem> mostSimilarItems(long itemID, int howMany)
			throws TasteException
	{
		return delegate.mostSimilarItems(itemID, howMany);
	}

	@Override
	public List<RecommendedItem> mostSimilarItems(long itemID, int howMany,
			Rescorer<LongPair> rescorer) throws TasteException
	{
		return delegate.mostSimilarItems(itemID, howMany,rescorer);
	}

	@Override
	public List<RecommendedItem> mostSimilarItems(long[] itemIDs, int howMany)
			throws TasteException
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RecommendedItem> mostSimilarItems(long[] itemIDs, int howMany,
			Rescorer<LongPair> rescorer) throws TasteException
	{
		return delegate.mostSimilarItems(itemIDs, howMany,rescorer);
	}

	@Override
	public List<RecommendedItem> mostSimilarItems(long[] itemIDs, int howMany,
			boolean excludeItemIfNotSimilarToAll) throws TasteException
	{
		return delegate.mostSimilarItems(itemIDs, howMany,excludeItemIfNotSimilarToAll);
	}

	@Override
	public List<RecommendedItem> mostSimilarItems(long[] itemIDs, int howMany,
			Rescorer<LongPair> rescorer, boolean excludeItemIfNotSimilarToAll)
					throws TasteException
	{
		return delegate.mostSimilarItems(itemIDs, howMany,rescorer,excludeItemIfNotSimilarToAll);
	}

	@Override
	public List<RecommendedItem> recommendedBecause(long userID, long itemID,
			int howMany) throws TasteException
	{
		return delegate.recommendedBecause(userID, itemID,howMany);
	}

}
